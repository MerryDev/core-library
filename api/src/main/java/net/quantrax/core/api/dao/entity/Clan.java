package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;

public interface Clan extends Entity<Integer> {

    @NotNull String name();

    @NotNull String tag();

    int coins();

    int level();

    @NotNull ClanMember owner();

    @NotNull @UnmodifiableView Collection<ClanMember> members();

    void name(@NotNull String name);

    void tag(@NotNull String tag);

    void level(int level);

    void addCoins(int amount);

    void removeCoins(int amount);

    void coins(int coins);

    void transferOwnership(@NotNull ClanMember oldOwner, @NotNull ClanMember newOwner);
}
