package net.quantrax.core.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Language {

    GERMAN("Deutsch", "de", "de"),
    ENGLISH("English", "en", "us");

    private final String name;
    private final String iso;
    private final String country;

    public static @NotNull Language findByName(@NotNull String name) {
        return Arrays.stream(values())
                .filter(language -> language.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(GERMAN);
    }

    public static @NotNull Language findByIso(@NotNull String iso) {
        return Arrays.stream(values())
                .filter(language -> language.iso.equalsIgnoreCase(iso))
                .findFirst()
                .orElse(GERMAN);
    }
}