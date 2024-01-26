package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.Entity;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.UUID;

public interface Friendship extends Entity<Integer> {

    @NotNull UUID first();

    @NotNull UUID second();

    @NotNull Timestamp created();

    void accept();

    void dissolve();

}
