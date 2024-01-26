package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.entity.IntEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;

public interface Clan extends IntEntity {

    @NotNull @UnmodifiableView Collection<ClanMember> members();

    @NotNull @UnmodifiableView Collection<ClanRequest> requests();

    @NotNull @UnmodifiableView Collection<ClanInvite> invites();

}
