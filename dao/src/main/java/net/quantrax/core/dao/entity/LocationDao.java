package net.quantrax.core.dao.entity;

import de.chojo.sadu.wrapper.util.Row;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.quantrax.core.api.dao.entity.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationDao implements Location {

    private final String name;
    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final float pitch;
    private final float yaw;

    @Contract("_ -> new")
    public static @NotNull Location fromRow(@NotNull Row row) throws SQLException {
        return new LocationDao(row.getString("name"), row.getString("world"), row.getDouble("x"), row.getDouble("y"), row.getDouble("z"),
                row.getFloat("pitch"), row.getFloat("yaw"));
    }

    @Contract("_, _, _, _, _, _, _ -> new")
    public static @NotNull Location create(@NotNull String name, @NotNull String world, double x, double y, double z, float pitch, float yaw) {
        return new LocationDao(name, world, x, y, z, pitch, yaw);
    }

    @Override
    public @NotNull String identifier() {
        return name;
    }

    @Override
    public @NotNull String world() {
        return world;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public float pitch() {
        return pitch;
    }

    @Override
    public float yaw() {
        return yaw;
    }
}
