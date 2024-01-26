package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;

public interface Clan extends Entity<Integer> {

    @NotNull @UnmodifiableView Collection<ClanMember> members();

    @NotNull @UnmodifiableView Collection<ClanRequest> requests();

    @NotNull @UnmodifiableView Collection<ClanInvite> invites();

}
