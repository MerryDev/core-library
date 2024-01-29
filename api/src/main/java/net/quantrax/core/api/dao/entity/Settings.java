package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.Entity;

import java.util.UUID;

public interface Settings extends Entity<UUID> {

    boolean clanInvites();

    boolean friendRequests();

    boolean friendJump();

    boolean friendNotify();

    boolean teamNotify();

    void clanInvites(boolean allow);

    void friendRequests(boolean allow);

    void friendJump(boolean allow);

    void friendNotify(boolean allow);

    void teamNotify(boolean allow);

}
