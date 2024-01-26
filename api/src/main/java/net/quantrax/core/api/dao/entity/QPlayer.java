package net.quantrax.core.api.dao.entity;

import net.quantrax.core.api.dao.base.entity.UUIDEntity;
import net.quantrax.core.api.dao.type.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.sql.Timestamp;
import java.util.Collection;

public interface QPlayer extends UUIDEntity {

    @NotNull Timestamp firstOnline();

    @NotNull Timestamp lastOnline();

    @NotNull String name();

    int coins();

    @Nullable Clan clan();

    @Nullable Long discordId();

    @NotNull @UnmodifiableView Collection<Friendship> friendships();

    @NotNull @UnmodifiableView Collection<FriendRequest> friendRequests();

    @NotNull @UnmodifiableView Collection<ClanInvite> clanInvites();

    void clan(@NotNull Clan clan);

    void discordId(@NotNull Long discordId);

    void language(@NotNull Language language);

    void coins(int coins);

    void addCoins(int amount);

    void removeCoins(int amount);

    void inviteFriend(@NotNull FriendRequest request);

    void requestClan(@NotNull ClanRequest request);

    void acceptAllFriendRequests();

    void denyAllFriendRequests();

    void denyAllClanInvites();

}
