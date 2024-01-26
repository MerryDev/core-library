package net.quantrax.core.api.dao.base.entity;

import net.quantrax.core.api.dao.base.Entity;
import net.quantrax.core.api.dao.base.entity.base.PrimitiveEntity;
import org.jetbrains.annotations.NotNull;

public interface IntEntity extends Entity<Integer>, PrimitiveEntity {

    @Override
    @NotNull Integer identifier();

}
