package net.quantrax.core.api.dao.base.entity;

import net.quantrax.core.api.dao.base.Entity;
import net.quantrax.core.api.dao.base.entity.base.ComplexEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface UUIDEntity extends Entity<UUID>, ComplexEntity {

    @Override
    @NotNull UUID identifier();

}
