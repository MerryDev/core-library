package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.Entity;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.UUID;

public interface ClanRequest extends Entity<Integer> {

    @NotNull UUID requester();

    @NotNull Timestamp created();

    int clanId();

    ClanMember accept();

    void deny();

}
