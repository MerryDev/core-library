package net.quantrax.core.api.dao.base;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Repository<T> {

    void create(@NotNull T t);

    void save(@NotNull T t);

    void delete(@NotNull T t);

    @NotNull CompletableFuture<List<T>> findAll();

}
