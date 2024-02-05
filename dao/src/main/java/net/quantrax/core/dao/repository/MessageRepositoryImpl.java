package net.quantrax.core.dao.repository;

import de.chojo.sadu.wrapper.util.UpdateResult;
import net.quantrax.core.api.dao.entity.Message;
import net.quantrax.core.api.dao.repository.MessageRepository;
import net.quantrax.core.api.util.Log;
import net.quantrax.core.dao.entity.MessageDao;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static de.chojo.sadu.adapter.StaticQueryAdapter.builder;

public class MessageRepositoryImpl implements MessageRepository {

    @Override
    public void create(@NotNull Message message) {
        builder()
                .query("INSERT INTO utility.message (i18n, de, en) VALUE (?, ?, ?);")
                .parameter(stmt -> stmt.setString(message.identifier()).setString(message.german()).setString(message.english()))
                .insert().send()
                .exceptionally(throwable -> {
                    Log.severe("Creating message with i18n path %s failed with an exception: %s", message.identifier(), throwable.getMessage());
                    return new UpdateResult(0);
                });
    }

    @Override
    public void save(@NotNull Message message) {
        builder()
                .query("UPDATE utility.message SET de=?, en=? WHERE i18n=?;")
                .parameter(stmt -> stmt.setString(message.german()).setString(message.english()).setString(message.identifier()))
                .update().send()
                .exceptionally(throwable -> {
                    Log.severe("Updating message with i18n path %s failed with an exception: %s", message.identifier(), throwable.getMessage());
                    return new UpdateResult(0);
                });
    }

    @Override
    public void delete(@NotNull Message message) {
        builder()
                .query("DELETE FROM utility.message WHERE i18n=?;")
                .parameter(stmt -> stmt.setString(message.identifier()))
                .delete().send()
                .exceptionally(throwable -> {
                    Log.severe("Deleting message with i18n path %s failed with an exception: %s", message.identifier(), throwable.getMessage());
                    return new UpdateResult(0);
                })
                .thenAccept(updateResult -> {
                    if (updateResult.changed()) Log.info("Deleted message with i18n path %s", message.identifier());
                });
    }

    @Override
    public @NotNull CompletableFuture<List<Message>> findAll() {
        return builder(Message.class)
                .queryWithoutParams("SELECT * FROM utility.message;")
                .readRow(MessageDao::fromRow)
                .all();
    }

    @Override
    public Optional<Message> findByI18n(@NotNull String i18n) {
        return builder(Message.class)
                .query("SELECT * FROM utility.message WHERE i18n=?;")
                .parameter(stmt -> stmt.setString(i18n))
                .readRow(MessageDao::fromRow)
                .firstSync();
    }
}
