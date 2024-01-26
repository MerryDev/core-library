package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.Entity;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.UUID;

public interface FriendRequest extends Entity<Integer> {

    @NotNull UUID inviter();

    @NotNull UUID requested();

    @NotNull Timestamp created();

    void accept();

    void deny();

}
