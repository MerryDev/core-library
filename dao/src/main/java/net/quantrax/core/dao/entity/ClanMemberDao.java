package net.quantrax.core.dao.entity;

import de.chojo.sadu.wrapper.util.Row;
import de.chojo.sadu.wrapper.util.UpdateResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.quantrax.core.api.dao.entity.ClanMember;
import net.quantrax.core.api.dao.type.ClanRole;
import net.quantrax.core.api.util.Log;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;

import static de.chojo.sadu.adapter.StaticQueryAdapter.builder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClanMemberDao implements ClanMember {

    private final UUID uuid;
    private ClanRole role;

    @Contract("_ -> new")
    public static @NotNull ClanMember fromRow(@NotNull Row row) throws SQLException {
        return new ClanMemberDao(row.getUuidFromString("uuid"), ClanRole.findByName(row.getString("name")));
    }

    @Contract("_, _ -> new")
    public static @NotNull ClanMember create(@NotNull UUID uuid, @NotNull ClanRole role) {
        return new ClanMemberDao(uuid, role);
    }

    @Override
    public @NotNull UUID identifier() {
        return uuid;
    }

    @Override
    public @NotNull ClanRole role() {
        return role;
    }

    @Override
    public void promote() {
        updateRole(ClanRole.higher(role));
    }

    @Override
    public void demote() {
        updateRole(ClanRole.prior(role));
    }

    @Override
    public void leaveClan() {
        builder()
                .query("DELETE FROM clan.clan_member WHERE uuid=?;")
                .parameter(stmt -> stmt.setUuidAsString(uuid))
                .delete().send()
                .exceptionally(throwable -> {
                    Log.severe("Leaving clan by player with uuid %s failed with an exception: %s", uuid, throwable.getMessage());
                    return new UpdateResult(0);
                });
    }

    private void updateRole(@NotNull ClanRole role) {
        builder()
                .query("UPDATE clan.clan_member SET role=? WHERE uuid=?;")
                .parameter(stmt -> stmt.setString(role.name()).setUuidAsString(uuid))
                .update().send()
                .exceptionally(throwable -> {
                    Log.severe("Updating role to %s of clan member with uuid %s failed with an exception: %s", role.name(), uuid, throwable.getMessage());
                    return new UpdateResult(0);
                });
    }
}
