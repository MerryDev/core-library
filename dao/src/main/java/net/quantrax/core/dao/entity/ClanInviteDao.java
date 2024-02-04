package net.quantrax.core.dao.entity;

import de.chojo.sadu.wrapper.util.Row;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.quantrax.core.api.dao.Current;
import net.quantrax.core.api.dao.entity.ClanInvite;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClanInviteDao implements ClanInvite, Current {

    private final int id;
    private final UUID inviter;
    private final UUID requested;
    private final Timestamp created;
    private final int clanId;

    @Contract("_ -> new")
    public static @NotNull ClanInvite fromRow(@NotNull Row row) throws SQLException {
        return new ClanInviteDao(row.getInt("id"), row.getUuidFromString("inviter_uid"), row.getUuidFromString("requested_uid"),
                row.getTimestamp("created"), row.getInt("clan_id"));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull ClanInvite create(@NotNull UUID inviter, @NotNull UUID requested, int clanId) {
        return new ClanInviteDao(-1, inviter, requested, Current.currentTimestamp(), clanId);
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
    public int clanId() {
        return clanId;
    }
}
