package net.quantrax.core.api.dao.base;

import org.jetbrains.annotations.NotNull;

public interface Entity<T> {

    @NotNull T identifier();

}
