package net.quantrax.core.api.dao.repository;

import net.quantrax.core.api.dao.base.Repository;
import net.quantrax.core.api.dao.entity.Message;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface MessageRepository extends Repository<Message> {

    Optional<Message> findByI18n(@NotNull String i18n);

}
