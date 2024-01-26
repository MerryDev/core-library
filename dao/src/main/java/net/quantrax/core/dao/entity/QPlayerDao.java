package net.quantrax.core.dao.entity;

import com.google.common.collect.Lists;
import de.chojo.sadu.wrapper.util.Row;
import de.chojo.sadu.wrapper.util.UpdateResult;
import net.quantrax.core.api.dao.entity.*;
import net.quantrax.core.api.dao.type.Language;
import net.quantrax.core.api.util.Log;
import net.quantrax.core.api.util.Preconditions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.regex.Pattern;

import static de.chojo.sadu.adapter.StaticQueryAdapter.builder;

public class QPlayerDao implements QPlayer {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,16}$");

    private final UUID uuid;
    private final Timestamp firstOnline;
    private final Timestamp lastOnline;
    private final String name;
    private Long discordId;
    private Language language;
    private int coins;

    private Clan clan;
    private Collection<Friendship> friendships = Collections.emptyList();
    private Collection<FriendRequest> friendRequests = Collections.emptyList();
    private Collection<ClanInvite> clanInvites = Collections.emptyList();

    private QPlayerDao(UUID uuid, String name, Language language, Long discordId, int coins, Timestamp firstOnline, Timestamp lastOnline) {
        Preconditions.regex(NAME_PATTERN, name, "The given username %s is not a valid minecraft name", name);

        this.uuid = uuid;
        this.firstOnline = firstOnline;
        this.lastOnline = lastOnline;
        this.name = name;
        this.language = language;
        this.discordId = discordId;
        this.coins = coins;
    }

    // Begin public api

    @Contract("_ -> new")
    public static @NotNull QPlayer fromRow(@NotNull Row row) throws SQLException {
        return new QPlayerDao(row.getUuidFromString("uuid"), row.getString("name"), Language.findByName(row.getString("language")), row.getLong("discord_account_id"),
                row.getInt("coins"), row.getTimestamp("first_online"), row.getTimestamp("last_online"));
    }

    @Contract("_, _ -> new")
    public static @NotNull QPlayer create(@NotNull UUID uuid, @NotNull String name) {
        return new QPlayerDao(uuid, name, Language.GERMAN, null, 0, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
    }

    @Override
    public @NotNull UUID identifier() {
        return uuid;
    }

    @Override
    public @NotNull Timestamp firstOnline() {
        return firstOnline;
    }

    @Override
    public @NotNull Timestamp lastOnline() {
        return lastOnline;
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public int coins() {
        return coins;
    }

    @Override
    public @Nullable Clan clan() {
        return clan;
    }

    @Override
    public @Nullable Long discordId() {
        return discordId;
    }

    @Override
    public @NotNull @UnmodifiableView Collection<Friendship> friendships() {
        updateFriendships();
        return Collections.unmodifiableCollection(friendships);
    }

    @Override
    public @NotNull @UnmodifiableView Collection<FriendRequest> friendRequests() {
        return Collections.unmodifiableCollection(friendRequests);
    }

    @Override
    public @NotNull @UnmodifiableView Collection<ClanInvite> clanInvites() {
        updateClanInvites();
        return Collections.unmodifiableCollection(clanInvites);
    }

    @Override
    public void clan(@NotNull Clan clan) {
        Preconditions.state(this.clan == null, "The player currently is in a clan");
        this.clan = clan;
    }

    @Override
    public void discordId(@NotNull Long discordId) {
        this.discordId = discordId;
    }

    @Override
    public void language(@NotNull Language language) {
        Preconditions.state(this.language != language, "The new language is same ás the previous selected one");
        this.language = language;
    }

    @Override
    public void coins(int coins) {
        Preconditions.state(coins >= 0, "The value of coins cannot be negative. Given: %s", coins);
        this.coins = coins;
    }

    @Override
    public void addCoins(int amount) {
        Preconditions.state(amount >= 0, "A negative value of coins cannot be added. Given: %s", amount);
        this.coins += amount;
    }

    @Override
    public void removeCoins(int amount) {
        Preconditions.state(amount >= 0, "A negative value of coins cannot be removed. Given %s", amount);
        Preconditions.state(coins - amount >= 0, "The result of subtraction %s - %s cannot be negative. Given: %s", coins, amount, coins - amount);
        this.coins -= amount;
    }

    @Override
    public void inviteFriend(@NotNull FriendRequest request) {
        builder()
                .query("INSERT INTO friendships.friend_request (inviter_uid, requested_uid) VALUE (?, ?);")
                .parameter(stmt -> stmt.setUuidAsString(uuid).setUuidAsString(request.requested()))
                .insert().send()
                .exceptionally(throwable -> {
                    Log.severe("Inviting the player with uuid %s as a friend by player with uuid %s failed with an exception: %s", request.requested(), uuid, throwable.getMessage());
                    return new UpdateResult(0);
                });
    }

    @Override
    public void requestClan(@NotNull ClanRequest request) {
        builder()
                .query("INSERT INTO clan.clan_request (requester_uid, clan_id) VALUE (?, ?);")
                .parameter(stmt -> stmt.setUuidAsString(uuid).setInt(request.clanId()))
                .insert().send()
                .exceptionally(throwable -> {
                    Log.severe("Requesting clan with id %s to join by player with uuid %s failed with an exception: %s", request.clanId(), uuid, throwable.getMessage());
                    return new UpdateResult(0);
                });
    }

    @Override
    public void acceptAllFriendRequests() {
        builder(FriendRequest.class)
                .query("SELECT * FROM friendships.friend_request WHERE requested_uid=?;")
                .parameter(stmt -> stmt.setUuidAsString(uuid))
                .readRow(FriendRequestDao::fromRow)
                .all()
                .exceptionally(throwable -> {
                    Log.severe("Accepting all pending incoming friend requests of player with uuid %s failed with an exception: %s", uuid, throwable.getMessage());
                    return Collections.emptyList();
                })
                .thenApply(this::applyFriendships)
                .thenAccept(friendships -> this.friendships = Collections.unmodifiableCollection(friendships));
    }

    @Override
    public void denyAllFriendRequests() {
        builder()
                .query("DELETE FROM friendships.friend_request WHERE requested_uid=?;")
                .parameter(stmt -> stmt.setUuidAsString(uuid))
                .delete().send()
                .exceptionally(throwable -> {
                    Log.severe("Deleting all incoming friend requests from player with uuid %s failed with an exception: %s", uuid, throwable.getMessage());
                    return new UpdateResult(0);
                })
                .thenAccept($ -> this.friendRequests = Collections.emptyList());
    }

    @Override
    public void denyAllClanInvites() {
        builder()
                .query("DELETE FROM clan.clan_invite WHERE requested_uid=?;")
                .parameter(stmt -> stmt.setUuidAsString(uuid))
                .delete().send()
                .exceptionally(throwable -> {
                    Log.severe("Deleting all incoming clan invites from player with uuid %s failed with an exception: %s", uuid, throwable.getMessage());
                    return new UpdateResult(0);
                });
    }

    // Begin internal API

    private void updateFriendships() {
        builder(Friendship.class)
                .query("SELECT * FROM friendships.friendship WHERE first_uid=? OR second_uid=?;")
                .parameter(stmt -> stmt.setUuidAsString(uuid).setUuidAsString(uuid))
                .readRow(FriendshipDao::fromRow)
                .all()
                .exceptionally(throwable -> {
                    Log.severe("Fetching all friendships of player with uuid %s failed with an exception: %s", uuid, throwable.getMessage());
                    return Collections.emptyList();
                })
                .thenAccept(friendships -> this.friendships = Collections.unmodifiableCollection(friendships));
    }

    private void updateClanInvites() {
        builder(ClanInvite.class)
                .query("SELECT * FROM clan.clan_invite WHERE requested_uid=?;")
                .parameter(stmt -> stmt.setUuidAsString(uuid))
                .readRow(null)
                .all()
                .exceptionally(throwable -> {
                    Log.severe("Fetching all open clan invites to player with uuid %s failed with an exception: %s", uuid, throwable.getMessage());
                    return Collections.emptyList();
                })
                .thenAccept(invites -> this.clanInvites = Collections.unmodifiableCollection(invites));
    }

    private @NotNull Collection<Friendship> applyFriendships(@NotNull Collection<FriendRequest> friendRequests) {
        final Collection<Friendship> friendships = Lists.newArrayList(this.friendships);
        friendRequests.stream().map(FriendRequest::accept).forEach(friendships::add);

        return friendships;
    }
}