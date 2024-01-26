package net.quantrax.core.api.dao.base.entity;

import net.quantrax.core.api.dao.base.Entity;
import net.quantrax.core.api.dao.base.entity.base.ComplexEntity;
import org.jetbrains.annotations.NotNull;

public interface StringEntity extends Entity<String>, ComplexEntity {

    @Override
    @NotNull String identifier();

}
