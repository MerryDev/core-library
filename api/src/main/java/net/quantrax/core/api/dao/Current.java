package net.quantrax.core.api.dao;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.Instant;

public interface Current {

    static @NotNull Timestamp currentTimestamp() {
        return now();
    }

    default @NotNull Timestamp timestamp() {
        return now();
    }

    @Contract(" -> new")
    private static @NotNull Timestamp now() {
        return Timestamp.from(Instant.now());
    }

}
