package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Optional;

public interface QPlayer {

    Optional<Clan> clan();

    @NotNull @UnmodifiableView Collection<Friendship> friendships();

    @NotNull @UnmodifiableView Collection<FriendRequest> friendRequests();

    @NotNull @UnmodifiableView Collection<ClanInvite> clanInvites();

    void friendRequest(@NotNull FriendRequest request);

    void clanRequest(@NotNull ClanRequest request);

    void language(@NotNull Language language);

    void coins(int coins);

    void addCoins(int amount);

    void removeCoins(int amount);

    void acceptAllFriendRequests();

    void denyAllFriendRequests();

    void denyAllClanInvites();

}
