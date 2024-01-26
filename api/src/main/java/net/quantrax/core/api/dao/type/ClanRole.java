package net.quantrax.core.api.dao.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ClanRole {

    OWNER("Owner", "core.api.clan-role.owner", 1),
    ADMIN("Admin", "core.api.clan-role.admin", 2),
    MODERATOR("Moderator", "core.api.clan-role.moderator", 3),
    MEMBER("Member", "core.api.clan-role.member", 4);

    private final String name;
    private final String i18n;
    private final int priority;

    public static @NotNull ClanRole findByName(@NotNull String name) {
        return Arrays.stream(values())
                .filter(clanRole -> clanRole.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(MEMBER);
    }

    public static @NotNull ClanRole findByI18n(@NotNull String i18n) {
        return Arrays.stream(values())
                .filter(clanRole -> clanRole.i18n.equalsIgnoreCase(i18n))
                .findFirst()
                .orElse(MEMBER);
    }

    public static @NotNull ClanRole higher(@NotNull ClanRole current) {
        return switch (current) {
            case OWNER -> OWNER;
            case ADMIN, MODERATOR -> ADMIN;
            case MEMBER -> MODERATOR;
        };
    }

    public static @NotNull ClanRole prior(@NotNull ClanRole current) {
        return switch (current) {
            case OWNER -> OWNER;
            case ADMIN -> MODERATOR;
            case MODERATOR, MEMBER -> MEMBER;
        };
    }

}
