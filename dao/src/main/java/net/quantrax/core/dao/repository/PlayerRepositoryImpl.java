package net.quantrax.core.dao.repository;

import de.chojo.sadu.wrapper.util.UpdateResult;
import net.quantrax.core.api.dao.entity.OnlineTime;
import net.quantrax.core.api.dao.entity.QPlayer;
import net.quantrax.core.api.dao.entity.Settings;
import net.quantrax.core.api.dao.repository.PlayerRepository;
import net.quantrax.core.api.dao.type.Language;
import net.quantrax.core.api.util.Log;
import net.quantrax.core.dao.entity.QPlayerDao;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static de.chojo.sadu.adapter.StaticQueryAdapter.builder;

public class PlayerRepositoryImpl implements PlayerRepository {

    @Override
    public void create(@NotNull QPlayer qPlayer) {
        builder()
                .query("INSERT INTO api.player (uuid, name) VALUE (?, ?);")
                .parameter(stmt -> stmt.setUuidAsString(qPlayer.identifier()).setString(qPlayer.name()).setString(qPlayer.name()))
                .append()
                .query("INSERT INTO api.online_time (uuid) VALUE (?);")
                .parameter(stmt -> stmt.setUuidAsString(qPlayer.identifier()))
                .append()
                .query("INSERT INTO api.setting (uuid) VALUE (?);")
                .parameter(stmt -> stmt.setUuidAsString(qPlayer.identifier()))
                .insert().send()
                .exceptionally(throwable -> {
                    Log.severe("Creating a new player with uuid %s failed with an exception: %s", qPlayer.identifier(), throwable.getMessage());
                    return new UpdateResult(0);
                });
    }

    @Override
    public void save(@NotNull QPlayer qPlayer) {
        final OnlineTime onlineTime = qPlayer.onlineTime();
        final Settings settings = qPlayer.settings();

        builder()
                .query("UPDATE api.player SET name=?, coins=?, language=?, discord_account_id=?, last_online=? WHERE uuid=?;")
                .parameter(stmt -> stmt.setString(qPlayer.name()).setInt(qPlayer.coins()).setString(qPlayer.language().naming()).setLong(qPlayer.discordId())
                        .setTimestamp(qPlayer.lastOnline()).setUuidAsString(qPlayer.identifier()))
                .append()
                .query("UPDATE api.online_time SET lobby=?, chest_ffa=?, art_expert=?, escape=? WHERE uuid=?;")
                .parameter(stmt -> stmt.setLong(onlineTime.lobby()).setLong(onlineTime.chestFFA()).setLong(onlineTime.artExpert()).setLong(onlineTime.escape()).setUuidAsString(qPlayer.identifier()))
                .append()
                .query("UPDATE api.setting SET clan_invite=?, friend_request=?, friend_jump=?, friend_notify=?, team_notify=? WHERE uuid=?;")
                .parameter(stmt -> stmt.setBoolean(settings.clanInvites()).setBoolean(settings.friendRequests()).setBoolean(settings.friendJump()).setBoolean(settings.friendNotify())
                        .setBoolean(settings.teamNotify()).setUuidAsString(qPlayer.identifier()))
                .update().send()
                .exceptionally(throwable -> {
                    Log.severe("Updating player with uuid %s failed with an exception: %s", qPlayer.identifier(), throwable.getMessage());
                    return new UpdateResult(0);
                });
    }

    @Override
    public void delete(@NotNull QPlayer qPlayer) {
        builder()
                .query("DELETE FROM api.player WHERE uuid=?;")
                .parameter(stmt -> stmt.setUuidAsString(qPlayer.identifier()))
                .delete().send()
                .exceptionally(throwable -> {
                    Log.severe("Deleting player with uuid %s failed with an exception: %s", qPlayer.identifier(), throwable.getMessage());
                    return new UpdateResult(0);
                })
                .thenAccept(updateResult -> {
                    if (updateResult.changed()) Log.info("Deleted player with uuid %s and corresponding data sets", qPlayer.identifier());
                });
    }

    @Override
    public @NotNull CompletableFuture<List<QPlayer>> findAll() {
        return builder(QPlayer.class)
                .queryWithoutParams("SELECT * FROM api.player;")
                .readRow(QPlayerDao::fromRow)
                .all();
    }

    @Override
    public CompletableFuture<List<QPlayer>> findByLanguage(@NotNull Language language) {
        return builder(QPlayer.class)
                .query("SELECT * FROM api.player WHERE language=?;")
                .parameter(stmt -> stmt.setString(language.naming()))
                .readRow(QPlayerDao::fromRow)
                .all();
    }

    @Override
    public Optional<QPlayer> findByUUID(@NotNull UUID uuid) {
        return builder(QPlayer.class)
                .query("SELECT * FROM api.player WHERE uuid=?;")
                .parameter(stmt -> stmt.setUuidAsString(uuid))
                .readRow(QPlayerDao::fromRow)
                .firstSync();
    }

    @Override
    public Optional<QPlayer> findByName(@NotNull String name) {
        return builder(QPlayer.class)
                .query("SELECT * FROM api.player WHERE name=?;")
                .parameter(stmt -> stmt.setString(name))
                .readRow(QPlayerDao::fromRow)
                .firstSync();
    }

    @Override
    public Optional<QPlayer> findByDiscordId(@NotNull Long discordId) {
        return builder(QPlayer.class)
                .query("SELECT * FROM api.player WHERE discord_account_id=?;")
                .parameter(stmt -> stmt.setLong(discordId))
                .readRow(QPlayerDao::fromRow)
                .firstSync();
    }
}
