package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.entity.UUIDEntity;
import net.quantrax.core.api.dao.type.ClanRole;
import org.jetbrains.annotations.NotNull;

public interface ClanMember extends UUIDEntity {

    @NotNull ClanRole role();

    void promote();

    void demote();

}
