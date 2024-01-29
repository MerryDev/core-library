package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.Entity;

import java.util.UUID;

public interface OnlineTime extends Entity<UUID> {

    long lobby();

    long chestFFA();

    long artExpert();

    long escape();

    void lobby(long playtime);

    void chestFFA(long playtime);

    void artExpert(long playtime);

    void escape(long playtime);

}
