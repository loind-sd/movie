package com.cinema.analyticsservice.streams;

import com.cinema.analyticsservice.service.JsonSerde;
import com.cinema.common.dto.kafkaDto.MovieRatingAllTime;
import com.cinema.common.dto.kafkaDto.RatingAgg;
import com.cinema.common.dto.kafkaMessage.MovieRatingEvent;
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
public class MovieRatingAllTimeTopology {

    @Bean
    public KTable<String, MovieRatingAllTime> movieRatingAllTime(
            StreamsBuilder builder
    ) {

        KTable<String, MovieRatingAllTime> table =
                builder
                        .stream(
                                "movie.rating",
                                Consumed.with(
                                        Serdes.String(),
                                        new JsonSerde<>(MovieRatingEvent.class)
                                )
                        )
                        .groupBy(
                                (k, v) -> v.getMovieId(),
                                Grouped.with(
                                        Serdes.String(),
                                        new JsonSerde<>(MovieRatingEvent.class)
                                )
                        )
                        .aggregate(
                                RatingAgg::new,
                                (movieId, event, agg) -> {
                                    agg.add(event.getRate());
                                    return agg;
                                },
                                Materialized.<String, RatingAgg, KeyValueStore<Bytes, byte[]>>as("rating-alltime-store")
                                        .withKeySerde(Serdes.String())
                                        .withValueSerde(new JsonSerde<>(RatingAgg.class))
                        )
                        .mapValues((movieId, agg) ->
                                new MovieRatingAllTime(
                                        movieId,
                                        agg.avg(),
                                        agg.getCount()
                                )
                        );

        // 🔥 side-effect: publish ra topic analytics
        table
                .toStream()
                .to(
                        "analytics.movie.rating.alltime",
                        Produced.with(
                                Serdes.String(),
                                new JsonSerde<>(MovieRatingAllTime.class)
                        )
                );

        return table;
    }

}
