package net.quantrax.core.api.dao.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Entity<T> {

    protected final T identifier;

}
