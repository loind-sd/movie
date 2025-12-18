package com.cinema.movieservice.dto.es_model;

import com.cinema.common.constants.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDate;
import java.util.List;

@Document(indexName = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDocument {

    @Id
    private Long id;

    private String title;

    private List<String> actors;

    private List<String> genres;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateFormatConstants.C_YYYY_MM_DD)
//    private LocalDate releaseDate;

    // dùng cho autocomplete
    private String titleSuggest;
}

