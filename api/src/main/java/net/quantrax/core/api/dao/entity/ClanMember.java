package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.Entity;
import net.quantrax.core.api.dao.type.ClanRole;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface ClanMember extends Entity<UUID> {

    @NotNull ClanRole role();

    void promote();

    void demote();

    void leaveClan();

}
