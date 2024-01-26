package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.entity.StringEntity;
import org.jetbrains.annotations.NotNull;

public interface Location extends StringEntity {

    @NotNull String world();

    double x();

    double y();

    double z();

    float pitch();

    float yaw();

}
