CREATE TABLE chat(
                     id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                     chat_id_into_telegram BIGINT
);

CREATE TABLE CHANNEL(
                        id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                        last_video_http varchar,
                        videos_http varchar
);

CREATE TABLE CHANNEL_CHAT(
                             channel_id int REFERENCES channel(id),
                             chat_id int REFERENCES chat(id),
                             PRIMARY KEY(channel_id, chat_id)
);