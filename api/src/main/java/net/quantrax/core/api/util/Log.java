package net.quantrax.core.api.util;

import com.google.errorprone.annotations.FormatMethod;
import com.google.errorprone.annotations.FormatString;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class Log {

    private static final Logger LOGGER = Logger.getLogger(Log.class.getSimpleName());

    @FormatMethod
    public static void severe(@NotNull @FormatString String template, Object... args) {
        LOGGER.severe(String.format(template, args));
    }

    @FormatMethod
    public static void info(@NotNull @FormatString String template, Object... args) {
        LOGGER.info(String.format(template, args));
    }
}
