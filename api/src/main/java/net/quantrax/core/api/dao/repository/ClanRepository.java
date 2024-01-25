package net.quantrax.core.api.dao.repository;

import net.quantrax.core.api.dao.base.Repository;
import net.quantrax.core.api.dao.entity.Clan;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ClanRepository extends Repository<Clan> {

    Optional<Clan> findByName(@NotNull String name);

    Optional<Clan> findByTag(@NotNull String tag);

    CompletableFuture<List<Clan>> findByLevel(int level);

}
