package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.Entity;

import java.util.UUID;

public interface ClanMember extends Entity<UUID> {

    void promote();

    void demote();

}
