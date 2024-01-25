package net.quantrax.core.api.dao.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;

public interface Clan {

    @NotNull @UnmodifiableView Collection<ClanMember> members();

    @NotNull @UnmodifiableView Collection<ClanRequest> requests();

    @NotNull @UnmodifiableView Collection<ClanInvite> invites();

}
