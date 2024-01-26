package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.entity.IntEntity;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.UUID;

public interface ClanRequest extends IntEntity {

    @NotNull UUID requester();

    @NotNull Timestamp created();

    int clanId();

    ClanMember accept();

    void deny();

}
