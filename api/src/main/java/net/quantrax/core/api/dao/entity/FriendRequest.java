package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.entity.IntEntity;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.UUID;

public interface FriendRequest extends IntEntity {

    @NotNull UUID inviter();

    @NotNull UUID requested();

    @NotNull Timestamp created();

    @NotNull Friendship accept();

    void deny();

}
