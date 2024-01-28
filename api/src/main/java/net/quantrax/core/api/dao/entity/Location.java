package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.Entity;
import org.jetbrains.annotations.NotNull;

public interface Location extends Entity<String> {

    @NotNull String world();

    double x();

    double y();

    double z();

    float pitch();

    float yaw();

}
