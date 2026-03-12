package com.cinema.analyticsservice.streams;

import com.cinema.analyticsservice.service.JsonSerde;
import com.cinema.common.dto.kafkaMessage.MovieViewAnalytics;
import com.cinema.common.dto.kafkaMessage.MovieViewEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.time.Duration;
import java.time.ZoneId;

@Configuration
@EnableKafkaStreams
public class MovieViewStream {

    @Bean
    public KStream<String, MovieViewEvent> movieViewKStream(StreamsBuilder builder) {

        KStream<String, MovieViewEvent> stream =
                builder.stream(
                        "movie.view",
                        Consumed.with(Serdes.String(), new JsonSerde<>(MovieViewEvent.class))
                );

        stream
                .groupBy((key, value) -> value.getMovieId())
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(10)))
                .count()
                .toStream()
                .mapValues((key, count) -> new MovieViewAnalytics(
                        key.key(),
                        key.window().startTime().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        key.window().endTime().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        count
                ))
                .map((windowedKey, analytics) ->
                        KeyValue.pair(windowedKey.key(), analytics)
                )
                .to(
                        "analytics.movie.views",
                        Produced.with(
                                Serdes.String(),
                                new JsonSerde<>(MovieViewAnalytics.class)
                        )
                );

        return stream;
    }
}

