package net.quantrax.core.dao.entity;

import de.chojo.sadu.wrapper.util.Row;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.quantrax.core.api.dao.entity.Friendship;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FriendshipDao implements Friendship {

    private final int id;
    private final UUID first;
    private final UUID second;
    private final Timestamp created;

    @Contract("_ -> new")
    public static @NotNull Friendship fromRow(@NotNull Row row) throws SQLException {
        return new FriendshipDao(row.getInt("id"), row.getUuidFromString("first"), row.getUuidFromString("second"), row.getTimestamp("created"));
    }

    @Override
    public @NotNull Integer identifier() {
        return id;
    }

    @Override
    public @NotNull UUID first() {
        return first;
    }

    @Override
    public @NotNull UUID second() {
        return second;
    }

    @Override
    public @NotNull Timestamp created() {
        return created;
    }

    @Override
    public void accept() {
    }

    @Override
    public void dissolve() {
    }
}
