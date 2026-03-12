package com.cinema.analyticsservice.streams;

import com.cinema.analyticsservice.dto.InteractionAgg;
import com.cinema.analyticsservice.dto.UserMovieInteractionEvent;
import com.cinema.analyticsservice.service.JsonSerde;
import com.cinema.common.dto.kafkaDto.MovieRatingAllTime;
import com.cinema.common.dto.kafkaMessage.MovieRatingEvent;
import com.cinema.common.dto.kafkaMessage.MovieViewEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafkaStreams
public class UserMovieInteractionTopology {

    @Bean
    public KTable<String, InteractionAgg> userMovieInteraction(
            StreamsBuilder builder
    ) {

        KStream<String, UserMovieInteractionEvent> viewStream =
                builder.stream(
                                "movie.view",
                                Consumed.with(
                                        Serdes.String(),
                                        new JsonSerde<>(MovieViewEvent.class)
                                )
                        )
                        .mapValues(v ->
                                new UserMovieInteractionEvent(
                                        v.getUserId(),
                                        v.getMovieId(),
                                        1
                                )
                        );

        KStream<String, UserMovieInteractionEvent> ratingStream =
                builder.stream(
                                "movie.rating",
                                Consumed.with(
                                        Serdes.String(),
                                        new JsonSerde<>(MovieRatingEvent.class)
                                )
                        )
                        .mapValues(v ->
                                new UserMovieInteractionEvent(
                                        v.getUserId(),
                                        v.getMovieId(),
                                        v.getRate()
                                )
                        );

        KStream<String, UserMovieInteractionEvent> merged =
                viewStream.merge(ratingStream);

        KTable<String, InteractionAgg> table = merged
                .selectKey((k, v) -> v.getUserId() + "|" + v.getMovieId())
                .groupByKey(
                        Grouped.with(
                                Serdes.String(),
                                new JsonSerde<>(UserMovieInteractionEvent.class)
                        )
                )
                .aggregate(
                        InteractionAgg::new,
                        (key, event, agg) -> {
                            agg.add(event.getScore());
                            return agg;
                        },
                        Materialized.<String, InteractionAgg, KeyValueStore<Bytes, byte[]>>as(
                                        "user-movie-interaction-store"
                                )
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new JsonSerde<>(InteractionAgg.class))
                );

        table
                .toStream()
                .to(
                        "analytics.user.movie.interaction",
                        Produced.with(
                                Serdes.String(),
                                new JsonSerde<>(InteractionAgg.class)
                        )
                );

        return table;
    }

}
