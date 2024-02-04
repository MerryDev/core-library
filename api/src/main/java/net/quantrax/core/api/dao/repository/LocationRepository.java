package net.quantrax.core.api.dao.repository;

import net.quantrax.core.api.dao.base.Repository;
import net.quantrax.core.api.dao.entity.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface LocationRepository extends Repository<Location> {

    Optional<Location> findByName(@NotNull String name);

}
