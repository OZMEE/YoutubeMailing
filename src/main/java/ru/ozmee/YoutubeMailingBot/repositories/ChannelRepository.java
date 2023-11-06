package ru.ozmee.YoutubeMailingBot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ozmee.YoutubeMailingBot.models.Channel;

import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Integer> {
    Channel findById(int id);

    Optional<Channel> findByVideosHttp(String videosHttp);
}
