package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.entity.IntEntity;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.UUID;

public interface Friendship extends IntEntity {

    @NotNull UUID first();

    @NotNull UUID second();

    @NotNull Timestamp created();

    void dissolve();

}
