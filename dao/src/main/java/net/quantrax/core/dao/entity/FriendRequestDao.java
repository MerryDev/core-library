package net.quantrax.core.dao.entity;

import de.chojo.sadu.wrapper.util.Row;
import de.chojo.sadu.wrapper.util.UpdateResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.quantrax.core.api.dao.Current;
import net.quantrax.core.api.dao.entity.FriendRequest;
import net.quantrax.core.api.dao.entity.Friendship;
import net.quantrax.core.api.util.Log;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static de.chojo.sadu.adapter.StaticQueryAdapter.builder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FriendRequestDao implements FriendRequest, Current {

    private final int id;
    private final UUID inviter;
    private final UUID requested;
    private final Timestamp created;

    // Begin public api

    @Contract("_ -> new")
    public static @NotNull FriendRequest fromRow(@NotNull Row row) throws SQLException {
        return new FriendRequestDao(row.getInt("id"), row.getUuidFromBytes("inviter_uid"), row.getUuidFromBytes("requested_uid"), row.getTimestamp("created"));
    }

    @Contract("_, _ -> new")
    public static @NotNull FriendRequest create(@NotNull UUID inviter, @NotNull UUID requested) {
        return new FriendRequestDao(-1, inviter, requested, Current.currentTimestamp());
    }

    @Override
    public @NotNull Integer identifier() {
        return id;
    }

    @Override
    public @NotNull UUID inviter() {
        return inviter;
    }

    @Override
    public @NotNull UUID requested() {
        return requested;
    }

    @Override
    public @NotNull Timestamp created() {
        return created;
    }

    @Override
    public @NotNull Friendship accept() {
        builder()
                .query("INSERT INTO friendships.friendship (first_uid, second_uid) VALUE (?, ?);")
                .parameter(stmt -> stmt.setUuidAsString(inviter).setUuidAsString(requested))
                .insert().send()
                .exceptionally(throwable -> {
                    Log.severe("Making friends between player with uuid %s and player with uuid %s failed with an exception: %s", inviter, requested, throwable.getMessage());
                    return new UpdateResult(0);
                })
                .thenCompose(this::deleteFriendRequest)
                .thenAccept($ -> Log.info("Player with uuid %s and player with uuid %s are now friends", inviter, requested));

        return FriendshipDao.create(inviter, requested);
    }

    @Override
    public void deny() {
        deleteFriendRequest(null);
    }

    // Begin internal api

    private CompletableFuture<UpdateResult> deleteFriendRequest(UpdateResult previousResult) {
        return builder()
                .query("DELETE FROM friendships.friend_request WHERE id=? OR (inviter_uid=? AND requested_uid=?);")
                .parameter(stmt -> stmt.setInt(id).setUuidAsString(inviter).setUuidAsString(requested))
                .delete().send()
                .exceptionally(throwable -> {
                    Log.severe("Removing the friend request by player with uuid %s to player with uuid %s failed with an exception: %s", inviter, requested, throwable.getMessage());
                    return new UpdateResult(0);
                });
    }
}
