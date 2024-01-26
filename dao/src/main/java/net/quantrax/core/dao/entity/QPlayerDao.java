package net.quantrax.core.dao.entity;

import de.chojo.sadu.wrapper.util.UpdateResult;
import net.quantrax.core.api.dao.entity.*;
import net.quantrax.core.api.dao.type.Language;
import net.quantrax.core.api.util.Log;
import net.quantrax.core.api.util.Preconditions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

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
        return Collections.unmodifiableCollection(clanInvites);
    }

    @Override
    public void friendRequest(@NotNull FriendRequest request) {
    }

    @Override
    public void discordId(@NotNull Long discordId) {
        this.discordId = discordId;
    }

    @Override
    public void clanRequest(@NotNull ClanRequest request) {
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
                .thenApply(friendRequests -> this.friendRequests = Collections.unmodifiableCollection(friendRequests))
                .thenAccept(friendRequests -> {
                    friendRequests.forEach(FriendRequest::accept);
                    denyAllFriendRequests();
                });
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
                });
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
}