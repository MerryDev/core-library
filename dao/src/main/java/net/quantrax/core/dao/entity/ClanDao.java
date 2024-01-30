package net.quantrax.core.dao.entity;

import de.chojo.sadu.wrapper.util.Row;
import de.chojo.sadu.wrapper.util.UpdateResult;
import net.quantrax.core.api.dao.entity.Clan;
import net.quantrax.core.api.dao.entity.ClanMember;
import net.quantrax.core.api.dao.type.ClanRole;
import net.quantrax.core.api.util.Log;
import net.quantrax.core.api.util.Preconditions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static de.chojo.sadu.adapter.StaticQueryAdapter.builder;

public class ClanDao implements Clan {

    private final int id;
    private String name;
    private String tag;
    private int coins;
    private int level;

    private ClanMember owner;
    private Collection<ClanMember> members;

    private ClanDao(int id, @NotNull String name, @NotNull String tag, int coins, int level) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.coins = coins;
        this.level = level;

        updateOwner();
    }

    @Contract("_ -> new")
    public static @NotNull Clan fromRow(@NotNull Row row) throws SQLException {
        return new ClanDao(row.getInt("id"), row.getString("name"), row.getString("tag"), row.getInt("coins"), row.getInt("level"));
    }

    @Override
    public @NotNull Integer identifier() {
        return id;
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull String tag() {
        return tag;
    }

    @Override
    public int coins() {
        return coins;
    }

    @Override
    public int level() {
        return level;
    }

    @Override
    public @NotNull ClanMember owner() {
        return owner;
    }

    @Override
    public @NotNull @UnmodifiableView Collection<ClanMember> members() {
        updateMembers();
        return Collections.unmodifiableCollection(members);
    }

    @Override
    public void name(@NotNull String name) {
        Preconditions.state(!name.isBlank(), "The name cannot be blank");
        Preconditions.state(name.length() >= 3 && name.length() <= 15, "");
        Preconditions.state(!this.name.equalsIgnoreCase(name), "The new name cannot be the old name");
        Preconditions.state(isNameAvailable(name), "The new name is already is use");
        this.name = name;
    }

    @Override
    public void tag(@NotNull String tag) {
        Preconditions.state(!tag.isBlank(), "The tag cannot be blank");
        Preconditions.state(tag.length() != 3, "The tag length must be three");
        Preconditions.state(isTagAvailable(tag), "The new tag is already in use");
        this.tag = tag;
    }

    @Override
    public void level(int level) {
        Preconditions.state(level > 1, "A level must be greater than one");
        Preconditions.state(this.level != level, "The new level cannot be the old level");
        this.level = level;
    }

    @Override
    public void addCoins(int amount) {
        Preconditions.state(amount >= 0, "Negative values cannot be added");
        this.coins += amount;
    }

    @Override
    public void removeCoins(int amount) {
        Preconditions.state(amount >= 0, "Negative values cannot be subtracted");
        this.coins = Math.max(coins - amount, 0);
    }

    @Override
    public void coins(int coins) {
        Preconditions.state(coins >= 0, "Coins cannot be negative");
        this.coins = coins;
    }

    @Override
    public void transferOwnership(@NotNull ClanMember oldOwner, @NotNull ClanMember newOwner) {
        builder()
                .query("UPDATE clan.clan_member SET role=? WHERE uuid=?;") // Demote old owner
                .parameter(stmt -> stmt.setString(ClanRole.ADMIN.naming()).setUuidAsString(oldOwner.identifier()))
                .append()
                .query("UPDATE clan.clan_member SET role=? WHERE uuid=?;") // Promoting new owner
                .parameter(stmt -> stmt.setString(ClanRole.OWNER.naming()).setUuidAsString(newOwner.identifier()))
                .update().send()
                .exceptionally(throwable -> {
                    Log.severe("Transferring the ownership of clan with id %s from player with uuid %s to player with uuid %s failed with an exception: %s",
                            id, oldOwner.identifier(), newOwner.identifier(), throwable.getMessage());
                    return new UpdateResult(0);
                })
                .thenAccept(result -> {
                    if (!result.changed()) return;
                    this.owner = newOwner;
                });
    }

    private void updateMembers() {
        builder(ClanMember.class)
                .query("SELECT * FROM clan.clan_member WHERE clan_id=?;")
                .parameter(stmt -> stmt.setInt(id))
                .readRow(ClanMemberDao::fromRow)
                .all()
                .exceptionally(throwable -> {
                    Log.severe("Fetching all current members of clan with id %s failed with an exception: %s", id, throwable.getMessage());
                    return Collections.emptyList();
                })
                .thenAccept(clanMembers -> this.members = Collections.unmodifiableCollection(clanMembers));
    }

    private void updateOwner() {
        builder(ClanMember.class)
                .query("SELECT * FROM clan.clan_member WHERE role=? AND clan_id=?;")
                .parameter(stmt -> stmt.setString(ClanRole.OWNER.naming()).setInt(id))
                .readRow(ClanMemberDao::fromRow)
                .first()
                .exceptionally(throwable -> {
                    Log.severe("Fetching the owner of clan with id %s failed with an exception: %s", id, throwable.getMessage());
                    return Optional.empty();
                })
                .thenAccept(optional -> optional.ifPresentOrElse(owner -> this.owner = owner, optional::orElseThrow));
    }

    private boolean isNameAvailable(@NotNull String name) {
        return builder(Boolean.class)
                .query("SELECT name FROM clan.clan WHERE name=?;")
                .parameter(stmt -> stmt.setString(name))
                .readRow(row -> row.getString("name") == null)
                .firstSync().orElse(true);
    }

    private boolean isTagAvailable(@NotNull String tag) {
        return builder(Boolean.class)
                .query("SELECT tag FROM clan.clan WHERE name=?;")
                .parameter(stmt -> stmt.setString(tag))
                .readRow(row -> row.getString("tag") == null)
                .firstSync().orElse(true);
    }
}
