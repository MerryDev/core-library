package net.quantrax.core.dao.entity;

import de.chojo.sadu.wrapper.util.Row;
import de.chojo.sadu.wrapper.util.UpdateResult;
import lombok.AllArgsConstructor;
import net.quantrax.core.api.dao.Current;
import net.quantrax.core.api.dao.entity.ClanMember;
import net.quantrax.core.api.dao.entity.ClanRequest;
import net.quantrax.core.api.dao.type.ClanRole;
import net.quantrax.core.api.util.Log;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import static de.chojo.sadu.adapter.StaticQueryAdapter.builder;

@AllArgsConstructor
public class ClanRequestDao implements ClanRequest, Current {

    private final int id;
    private final int clanId;
    private final UUID requester;
    private final Timestamp created;

    @Contract("_ -> new")
    public static @NotNull ClanRequest fromRow(@NotNull Row row) throws SQLException {
        return new ClanRequestDao(row.getInt("id"), row.getInt("clan_id"), row.getUuidFromString("requester_uid"), row.getTimestamp("created"));
    }

    public static @NotNull ClanRequest create(@NotNull UUID requester, int clanId) {
        return new ClanRequestDao(-1, clanId, requester, Current.currentTimestamp());
    }

    @Override
    public @NotNull Integer identifier() {
        return id;
    }

    @Override
    public @NotNull UUID requester() {
        return requester;
    }

    @Override
    public @NotNull Timestamp created() {
        return created;
    }

    @Override
    public int clanId() {
        return clanId;
    }

    @Override
    public ClanMember accept() {
        builder()
                .query("INSERT INTO clan.clan_member (uuid, role, clan_id) VALUE (?, ?, ?);")
                .parameter(stmt -> stmt.setUuidAsString(requester).setString(ClanRole.MEMBER.name()).setInt(clanId))
                .insert().send()
                .exceptionally(throwable -> {
                    Log.severe("Accepting the request of joining clan with id %s by player with uuid %s failed with an exception: %s", clanId, requester, throwable.getMessage());
                    return new UpdateResult(0);
                });

        return builder(ClanMember.class)
                .query("SELECT * FROM clan.clan_member WHERE uuid=?;")
                .parameter(stmt -> stmt.setUuidAsString(requester))
                .readRow(ClanMemberDao::fromRow)
                .firstSync().orElseThrow(() -> new RuntimeException("The newly created clan member could not be fetched from database"));
    }

    @Override
    public void deny() {
        builder()
                .query("DELETE FROM clan.clan_request WHERE id=? OR requester_uid=?;")
                .parameter(stmt -> stmt.setInt(id).setUuidAsString(requester))
                .delete().send()
                .exceptionally(throwable -> {
                    Log.severe("Deleting friend request with id %s failed with an exception: %s", id, throwable.getMessage());
                    return new UpdateResult(0);
                });
    }
}
