package com.cinema.movieservice.service.impl;

import com.cinema.common.base.ServiceResult;
import com.cinema.common.enums.PersonRole;
import com.cinema.common.exception.ErrorCode;
import com.cinema.common.service.MinioService;
import com.cinema.common.service.RedisService;
import com.cinema.movieservice.dto.request.CreateMovieRequest;
import com.cinema.movieservice.dto.request.UpdateMovieRequest;
import com.cinema.movieservice.dto.response.MovieDetailResponse;
import com.cinema.movieservice.dto.response.PeopleResponse;
import com.cinema.movieservice.entity.Movie;
import com.cinema.movieservice.entity.MovieGenres;
import com.cinema.movieservice.repository.MovieGenresRepository;
import com.cinema.movieservice.repository.MoviePeopleRepository;
import com.cinema.movieservice.repository.MovieRepository;
import com.cinema.movieservice.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieGenresRepository movieGenresRepository;
    private final MoviePeopleRepository moviePeopleRepository;
    private final MinioService minioService;

    @Override
    public ServiceResult create(MultipartFile poster, CreateMovieRequest request) {
        log.debug("Create Movie");
        Movie movie = new Movie();
        BeanUtils.copyProperties(request, movie);

        if (poster != null) {
            try {
                String objectPath = minioService.buildMoviePosterPath(new Random().nextLong());
                String posterUrl = minioService.upload(objectPath, poster.getInputStream(), poster.getSize(), poster.getContentType());
                movie.setPosterUrl(posterUrl);
                log.debug("Uploaded poster to URL: {}", posterUrl);
            } catch (Exception e) {
                log.error("Failed to upload poster", e);
            }
        }

        movieRepository.save(movie);

        if (request.genreIds() != null && !request.genreIds().isEmpty()) {
            List<MovieGenres> movieGenres = new ArrayList<>();
            request.genreIds().forEach(genre -> {
                MovieGenres movieGenre = new MovieGenres();
                movieGenre.setMovieId(movie.getId());
                movieGenre.setGenreId(genre);
                movieGenres.add(movieGenre);
            });
        }

        log.debug("Movie created : {}", movie.getTitle());
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult update(MultipartFile poster, UpdateMovieRequest request) {
        log.debug("Update Movie");
        Optional<Movie> optionalMovie = movieRepository.findById(request.id());
        if (optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();
            BeanUtils.copyProperties(request, movie);

            if (poster != null) {
                try {
                    String objectPath = minioService.buildMoviePosterPath(new Random().nextLong());
                    String posterUrl = minioService.upload(objectPath, poster.getInputStream(), poster.getSize(), poster.getContentType());
                    movie.setPosterUrl(posterUrl);
                    log.debug("Uploaded poster to URL: {}", posterUrl);
                } catch (Exception e) {
                    log.error("Failed to upload poster", e);
                }
            }

            movieRepository.save(movie);
            log.debug("Movie updated : {}", movie.getTitle());
            return ServiceResult.ok();
        }
        return ServiceResult.fail(ErrorCode.MOVIE_NOT_FOUND);
    }

    @Override
    public ServiceResult delete(Long movieId) {
        log.debug("Delete Movie");
        Optional<Movie> optionalMovie = movieRepository.findById(movieId);
        if (optionalMovie.isPresent()) {
            movieRepository.deleteById(movieId);
            log.debug("Movie deleted with ID: {}", movieId);
            return ServiceResult.ok();
        }
        return ServiceResult.fail(ErrorCode.MOVIE_NOT_FOUND);
    }

    @Override
    public ServiceResult getDetail(Long movieId) {
        log.debug("Get Movie Detail");
        Optional<Movie> optionalMovie = movieRepository.findById(movieId);
        if (optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();
            MovieDetailResponse response = new MovieDetailResponse();
            BeanUtils.copyProperties(movie, response);
            if (StringUtils.isNotBlank(movie.getPosterUrl())) {
                response.setPosterUrl(minioService.generatePresignedUrl(movie.getPosterUrl(), 60));
            }

            response.setGenres(movieGenresRepository.findAllByMovieId(movieId));
            List<PeopleResponse> people = moviePeopleRepository.findByMovieId(movieId);
            List<PeopleResponse> directors = new ArrayList<>();
            List<PeopleResponse> actors = new ArrayList<>();

            people.forEach(p -> {
                if (Objects.equals(p.getRole(), PersonRole.DIRECTOR)) {
                    directors.add(p);
                } else if (Objects.equals(p.getRole(), PersonRole.ACTOR)) {
                    actors.add(p);
                }
            });

            response.setDirectors(directors);
            response.setActors(actors);

            log.debug("Fetched movie details for ID: {}", movieId);
            return ServiceResult.ok(response);
        }
        return ServiceResult.fail(ErrorCode.MOVIE_NOT_FOUND);
    }
}
