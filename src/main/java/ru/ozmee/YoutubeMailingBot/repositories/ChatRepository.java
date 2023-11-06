package ru.ozmee.YoutubeMailingBot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ozmee.YoutubeMailingBot.models.Chat;

import java.util.Optional;


@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    Optional<Chat> findByChatId(Long chatId);
}
