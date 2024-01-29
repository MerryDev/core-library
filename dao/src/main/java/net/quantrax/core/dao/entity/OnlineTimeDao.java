package net.quantrax.core.dao.entity;

import de.chojo.sadu.wrapper.util.Row;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.quantrax.core.api.dao.entity.OnlineTime;
import net.quantrax.core.api.util.Preconditions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OnlineTimeDao implements OnlineTime {

    private final UUID uuid;
    private long lobby;
    private long chestFFA;
    private long artExpert;
    private long escape;

    @Contract("_ -> new")
    public static @NotNull OnlineTime fromRow(@NotNull Row row) throws SQLException {
        return new OnlineTimeDao(row.getUuidFromString("uuid"), row.getLong("lobby"), row.getLong("chest_ffa"),
                row.getLong("art_expert"), row.getLong("escape"));
    }

    @Override
    public @NotNull UUID identifier() {
        return uuid;
    }

    @Override
    public long lobby() {
        return lobby;
    }

    @Override
    public long chestFFA() {
        return chestFFA;
    }

    @Override
    public long artExpert() {
        return artExpert;
    }

    @Override
    public long escape() {
        return escape;
    }

    @Override
    public void lobby(long playtime) {
        assertValidPlaytime(playtime);
        this.lobby += playtime;
    }

    @Override
    public void chestFFA(long playtime) {
        assertValidPlaytime(playtime);
        this.chestFFA += playtime;
    }

    @Override
    public void artExpert(long playtime) {
        assertValidPlaytime(playtime);
        this.artExpert += playtime;
    }

    @Override
    public void escape(long playtime) {
        assertValidPlaytime(playtime);
        this.escape += playtime;
    }

    private void assertValidPlaytime(long playtime) {
        Preconditions.state(playtime > 0, "The playtime cannot be negative. Given: %s", playtime);
    }
}
