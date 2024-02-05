package net.quantrax.core.dao.repository;

import de.chojo.sadu.wrapper.util.UpdateResult;
import net.quantrax.core.api.dao.entity.Location;
import net.quantrax.core.api.dao.repository.LocationRepository;
import net.quantrax.core.api.util.Log;
import net.quantrax.core.dao.entity.LocationDao;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static de.chojo.sadu.adapter.StaticQueryAdapter.builder;

public class LocationRepositoryImpl implements LocationRepository {

    @Override
    public void create(@NotNull Location location) {
        builder()
                .query("INSERT INTO utility.location (name, world, x, y, z, pitch, yaw) VALUE (?, ?, ?, ?, ?, ?, ?);")
                .parameter(stmt -> stmt.setString(location.identifier()).setString(location.world()).setDouble(location.x()).setDouble(location.y()).setDouble(location.z())
                        .setFloat(location.pitch()).setFloat(location.yaw()))
                .insert().send()
                .exceptionally(throwable -> {
                    Log.severe("Creating location with name %s failed with an exception: %s", location.identifier(), throwable.getMessage());
                    return new UpdateResult(0);
                });
    }

    @Override
    public void save(@NotNull Location location) {
        builder()
                .query("UPDATE utility.location SET world=?, x=?, y=?, z=?, pitch=?, yaw=? WHERE name=?;")
                .parameter(stmt -> stmt.setString(location.world()).setDouble(location.x()).setDouble(location.y()).setDouble(location.z())
                        .setFloat(location.pitch()).setFloat(location.yaw()).setString(location.identifier()))
                .update().send()
                .exceptionally(throwable -> {
                    Log.severe("Updating location with name %s failed with an exception: %s", location.identifier(), throwable.getMessage());
                    return new UpdateResult(0);
                });
    }

    @Override
    public void delete(@NotNull Location location) {
        builder()
                .query("DELETE FROM utility.location WHERE name=?;")
                .parameter(stmt -> stmt.setString(location.identifier()))
                .delete().send()
                .exceptionally(throwable -> {
                    Log.severe("Deleting location with name %s failed with an exception: %s", location.identifier(), throwable.getMessage());
                    return new UpdateResult(0);
                })
                .thenAccept(updateResult -> {
                    if (updateResult.changed()) Log.info("Deleted location with name %s", location.identifier());
                });
    }

    @Override
    public @NotNull CompletableFuture<List<Location>> findAll() {
        return builder(Location.class)
                .queryWithoutParams("SELECT * FROM utility.location;")
                .readRow(LocationDao::fromRow)
                .all();
    }

    @Override
    public Optional<Location> findByName(@NotNull String name) {
        return builder(Location.class)
                .query("SELECT * FROM utility.location WHERE name=?;")
                .parameter(stmt -> stmt.setString(name))
                .readRow(LocationDao::fromRow)
                .firstSync();
    }
}
