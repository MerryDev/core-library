package net.quantrax.core.api.dao.repository;

import net.quantrax.core.api.dao.base.Repository;
import net.quantrax.core.api.dao.entity.QPlayer;
import net.quantrax.core.api.dao.type.Language;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerRepository extends Repository<QPlayer> {

    Optional<QPlayer> findByUUID(@NotNull UUID uuid);

    Optional<QPlayer> findByName(@NotNull String name);

    Optional<QPlayer> findByDiscordId(@NotNull Long discordId);

    CompletableFuture<List<QPlayer>> findByLanguage(@NotNull Language language);

}
