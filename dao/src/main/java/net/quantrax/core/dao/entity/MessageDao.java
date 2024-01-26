package net.quantrax.core.dao.entity;

import de.chojo.sadu.wrapper.util.Row;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.quantrax.core.api.dao.entity.Message;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageDao implements Message {

    private final String i18n;
    private final String german;
    private final String english;

    @Contract("_ -> new")
    public static @NotNull MessageDao fromRow(@NotNull Row row) throws SQLException {
        return new MessageDao(row.getString("i18n"), row.getString("de"), row.getString("en"));
    }

    @Override
    public @NotNull String identifier() {
        return i18n;
    }

    @Override
    public String german() {
        return german;
    }

    @Override
    public String english() {
        return english;
    }
}
