package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.Entity;

public interface ClanRequest extends Entity<Integer> {

    void accept();

    void deny();

}
