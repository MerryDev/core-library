package net.quantrax.core.dao.entity;

import de.chojo.sadu.wrapper.util.Row;
import de.chojo.sadu.wrapper.util.UpdateResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.quantrax.core.api.dao.Current;
import net.quantrax.core.api.dao.entity.Friendship;
import net.quantrax.core.api.util.Log;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import static de.chojo.sadu.adapter.StaticQueryAdapter.builder;

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

    @Contract("_, _ -> new")
    public static @NotNull Friendship create(@NotNull UUID first, @NotNull UUID second) {
        return new FriendshipDao(-1, first, second, Current.currentTimestamp());
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
    public void dissolve() {
        builder()
                .query("DELETE FROM friendships.friendship WHERE id=? OR (first_uid=? AND second_uid=?);")
                .parameter(stmt -> stmt.setInt(id).setUuidAsString(first).setUuidAsString(second))
                .delete().send()
                .exceptionally(throwable -> {
                    Log.severe("Dissolving the friendship between player with uuid %s and player with uuid %s failed with an exception: %s", first, second, throwable.getMessage());
                    return new UpdateResult(0);
                });
    }
}
