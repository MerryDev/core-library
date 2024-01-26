package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.Entity;
import net.quantrax.core.api.dao.type.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;

public interface QPlayer extends Entity<UUID> {

    @NotNull Timestamp firstOnline();

    @NotNull Timestamp lastOnline();

    @NotNull String name();

    int coins();

    @Nullable Clan clan();

    @Nullable Long discordId();

    @NotNull @UnmodifiableView Collection<Friendship> friendships();

    @NotNull @UnmodifiableView Collection<FriendRequest> friendRequests();

    @NotNull @UnmodifiableView Collection<ClanInvite> clanInvites();

    void friendRequest(@NotNull FriendRequest request);

    void discordId(@NotNull Long discordId);

    void clanRequest(@NotNull ClanRequest request);

    void language(@NotNull Language language);

    void coins(int coins);

    void addCoins(int amount);

    void removeCoins(int amount);

    void acceptAllFriendRequests();

    void denyAllFriendRequests();

    void denyAllClanInvites();

}
