package com.cinema.movieservice.service.impl;

import com.cinema.common.base.ServiceResult;
import com.cinema.common.dto.PageResult;
import com.cinema.common.enums.PersonRole;
import com.cinema.common.exception.ErrorCode;
import com.cinema.common.service.MinioService;
import com.cinema.movieservice.dto.es_model.MovieDocument;
import com.cinema.movieservice.dto.request.movie.CreateMovieRequest;
import com.cinema.movieservice.dto.request.movie.UpdateMovieRequest;
import com.cinema.movieservice.dto.response.MovieDetailResponse;
import com.cinema.movieservice.dto.response.PeopleResponse;
import com.cinema.movieservice.entity.Movie;
import com.cinema.movieservice.entity.MovieGenres;
import com.cinema.movieservice.entity.MoviePeople;
import com.cinema.movieservice.mapper.MovieMapper;
import com.cinema.movieservice.repository.MovieGenresRepository;
import com.cinema.movieservice.repository.MoviePeopleRepository;
import com.cinema.movieservice.repository.MovieRepository;
import com.cinema.movieservice.service.MovieService;
import com.cinema.movieservice.service.searchES.MovieSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieSearchService movieSearchService;
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
                movieGenresRepository.saveAll(movieGenres);
            });
        }

        if (request.castIds() != null && !request.castIds().isEmpty()) {
            List<MoviePeople> moviePeople = new ArrayList<>();
            request.castIds().forEach(castId -> {
                MoviePeople actor = new MoviePeople();
                actor.setMovieId(movie.getId());
                actor.setPeopleId(castId);
                actor.setRole(PersonRole.ACTOR.name());
                moviePeople.add(actor);
            });
            moviePeopleRepository.saveAll(moviePeople);
        }

        if (request.directorIds() != null && !request.directorIds().isEmpty()) {
            List<MoviePeople> moviePeople = new ArrayList<>();
            request.directorIds().forEach(directorId -> {
                MoviePeople director = new MoviePeople();
                director.setMovieId(movie.getId());
                director.setPeopleId(directorId);
                director.setRole(PersonRole.DIRECTOR.name());
                moviePeople.add(director);
            });
            moviePeopleRepository.saveAll(moviePeople);
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
                p.setAvatarUrl(minioService.generatePresignedUrl(p.getAvatarUrl(), 60));
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

    @Override
    public ServiceResult getWithPaging(String keyword) {
        try {
            PageResult<MovieDocument> pageResult = movieSearchService.search(keyword, null, null, null, PageRequest.of(0, 20));
            List<Long> ids = pageResult.getContent()
                    .stream().map(MovieDocument::getId)
                    .toList();

            List<Movie> movies = movieRepository.findAllById(ids);
            PageResult<Movie> moviePageResult = PageResult.<Movie>builder()
                    .page(pageResult.getPage())
                    .size(pageResult.getSize())
                    .total(pageResult.getTotal())
                    .content(movies)
                    .build();
            return ServiceResult.ok(moviePageResult);

//            return ServiceResult.ok(movieRepository.findByTitleContains(keyword));
        } catch (Exception e) {
            log.error("Failed to search movies", e);
        }
        return null;
    }
}
