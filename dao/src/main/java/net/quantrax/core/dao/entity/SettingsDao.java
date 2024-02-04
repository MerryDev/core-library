package net.quantrax.core.dao.entity;

import de.chojo.sadu.wrapper.util.Row;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.quantrax.core.api.dao.entity.Settings;
import net.quantrax.core.api.util.Preconditions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SettingsDao implements Settings {

    private final UUID uuid;
    private boolean clanInvites;
    private boolean friendRequests;
    private boolean friendJump;
    private boolean friendNotify;
    private boolean teamNotify;

    @Contract("_ -> new")
    public static @NotNull Settings fromRow(@NotNull Row row) throws SQLException {
        return new SettingsDao(row.getUuidFromString("uuid"), row.getBoolean("clan_invite"), row.getBoolean("friend_request"),
                row.getBoolean("friend_jump"), row.getBoolean("friend_notify"), row.getBoolean("team_notify"));
    }

    @Override
    public @NotNull UUID identifier() {
        return uuid;
    }

    @Override
    public boolean clanInvites() {
        return clanInvites;
    }

    @Override
    public boolean friendRequests() {
        return friendRequests;
    }

    @Override
    public boolean friendJump() {
        return friendJump;
    }

    @Override
    public boolean friendNotify() {
        return friendNotify;
    }

    @Override
    public boolean teamNotify() {
        return teamNotify;
    }

    @Override
    public void clanInvites(boolean allow) {
        Preconditions.state(clanInvites != allow, "Clan invites are already %s", state(clanInvites));
        this.clanInvites = allow;
    }

    @Override
    public void friendRequests(boolean allow) {
        Preconditions.state(friendRequests != allow, "Friend requests are already %s", state(friendRequests));
        this.friendRequests = allow;
    }

    @Override
    public void friendJump(boolean allow) {
        Preconditions.state(friendJump != allow, "Friend jumps are already %s", state(friendJump));
        this.friendJump = allow;
    }

    @Override
    public void friendNotify(boolean allow) {
        Preconditions.state(friendNotify != allow, "Friend notifications are already %s", state(friendNotify));
        this.friendNotify = allow;
    }

    @Override
    public void teamNotify(boolean allow) {
        Preconditions.state(teamNotify != allow, "Team notifications are already %s", state(teamNotify));
    }

    private @NotNull String state(boolean current) {
        return current ? "allowed" : "forbidden";
    }
}
