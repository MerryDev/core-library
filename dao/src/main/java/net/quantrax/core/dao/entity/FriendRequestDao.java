package net.quantrax.core.dao.entity;

import de.chojo.sadu.wrapper.util.Row;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.quantrax.core.api.dao.entity.FriendRequest;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FriendRequestDao implements FriendRequest {

    private final int id;
    private final UUID inviter;
    private final UUID requested;
    private final Timestamp created;

    @Contract("_ -> new")
    public static @NotNull FriendRequest fromRow(@NotNull Row row) throws SQLException {
        return new FriendRequestDao(row.getInt("id"), row.getUuidFromBytes("inviter_uid"), row.getUuidFromBytes("requested_uid"), row.getTimestamp("created"));
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
    public void accept() {

    }

    @Override
    public void deny() {

    }
}
