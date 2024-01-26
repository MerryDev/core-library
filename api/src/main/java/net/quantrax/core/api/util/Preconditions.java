package net.quantrax.core.api.util;

import com.google.errorprone.annotations.FormatMethod;
import com.google.errorprone.annotations.FormatString;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public final class Preconditions {

    @FormatMethod
    public static void state(boolean expression, @FormatString String template, Object... args) {
        if (!expression) throw new IllegalStateException(String.format(template, args));
    }

    @FormatMethod
    public static void regex(@NotNull Pattern pattern, @NotNull String check, @FormatString String template, Object... args) {
        if (!pattern.matcher(check).find()) throw new IllegalStateException(String.format(template, args));
    }

}
