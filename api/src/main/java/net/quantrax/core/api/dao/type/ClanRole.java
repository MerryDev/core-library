package net.quantrax.core.api.dao.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ClanRole {

    OWNER(1, "core.api.clan-role.owner"),
    ADMIN(2, "core.api.clan-role.admin"),
    MODERATOR(3, "core.api.clan-role.moderator"),
    MEMBER(4, "core.api.clan-role.member");

    private final int priority;
    private final String i18n;

    public static @NotNull ClanRole findByI18n(@NotNull String i18n) {
        return Arrays.stream(values())
                .filter(clanRole -> clanRole.i18n.equalsIgnoreCase(i18n))
                .findFirst()
                .orElse(MEMBER);
    }

}
