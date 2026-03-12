package com.cinema.movieservice.service.impl;

import com.cinema.common.base.ServiceResult;
import com.cinema.common.constants.CommonConstants;
import com.cinema.common.dto.PageResult;
import com.cinema.common.enums.PersonRole;
import com.cinema.common.exception.ErrorCode;
import com.cinema.common.service.MinioService;
import com.cinema.common.service.PrefixSearchSuggestionService;
import com.cinema.common.service.RedisService;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
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
    private final PrefixSearchSuggestionService prefixSearchSuggestionService;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;

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

        try {
            Object data = redisService.getValue(CommonConstants.RedisKey.PREFIX_MOVIE_DETAIL + movieId);
            if (Objects.equals(data, "NULL")) {
                log.info("Cache hit for movie detail (null), ID: {}", movieId);
                return ServiceResult.fail(ErrorCode.MOVIE_NOT_FOUND);
            }
            if (data != null) {
                log.info("Cache hit for movie detail, ID: {}", movieId);
                MovieDetailResponse response = (MovieDetailResponse) data;
                return ServiceResult.ok(response);
            }
        } catch (Exception e) {
            log.error("Failed to record analytics", e);
        }

        int retryCount = 1;
        while (retryCount++ < 3) {
            boolean lock = tryLock(CommonConstants.RedisKey.PREFIX_MOVIE_DETAIL_LOCK + movieId, 10, TimeUnit.SECONDS);
            if (lock) {
                try {
                    // Double check cache after acquiring lock
                    Object data = redisService.getValue(CommonConstants.RedisKey.PREFIX_MOVIE_DETAIL + movieId);
                    if (Objects.equals(data, "NULL")) {
                        log.info("Cache hit for movie detail (null) after lock, ID: {}", movieId);
                        return ServiceResult.fail(ErrorCode.MOVIE_NOT_FOUND);
                    }
                    if (data != null) {
                        log.info("Cache hit for movie detail after lock, ID: {}", movieId);
                        MovieDetailResponse response = (MovieDetailResponse) data;
                        return ServiceResult.ok(response);
                    }

                    // Cache miss, proceed to fetch from DB
                    log.info("Cache miss for movie detail, fetching from DB, ID: {}", movieId);
                    // đoạn này gọi db để lấy dữ liệu, sau đó cache lại, cuối cùng trả về response


                } catch (Exception e) {
                    log.error("Failed to get movie detail", e);
                } finally {
                    redisService.removeValue(CommonConstants.RedisKey.PREFIX_MOVIE_DETAIL_LOCK);
                }
            } else {
                log.info("Failed to acquire lock for movie detail, ID: {}, another thread is fetching data", movieId);
                try {
                    Thread.sleep(100); // Wait briefly before retrying
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // check thử xem đã có cache chưa sau khi chờ
                Object data = redisService.getValue(CommonConstants.RedisKey.PREFIX_MOVIE_DETAIL + movieId);
                if (Objects.equals(data, "NULL")) {
                    log.info("Cache hit for movie detail (null) after lock, ID: {}", movieId);
                    return ServiceResult.fail(ErrorCode.MOVIE_NOT_FOUND);
                }
                if (data != null) {
                    log.info("Cache hit for movie detail after lock, ID: {}", movieId);
                    MovieDetailResponse response = (MovieDetailResponse) data;
                    return ServiceResult.ok(response);
                }
            }
        }

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

            try {
                long ttl = 60 + ThreadLocalRandom.current().nextLong(30);
                redisService.setValueWithExpireTime(CommonConstants.RedisKey.PREFIX_MOVIE_DETAIL + movieId, response, ttl, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("Failed to cache movie detail", e);
            }

            log.debug("Fetched movie details for ID: {}", movieId);
            return ServiceResult.ok(response);
        } else {
            log.warn("Movie not found for ID: {}", movieId);
            try {
                redisService.setValueWithExpireTime(CommonConstants.RedisKey.PREFIX_MOVIE_DETAIL + movieId, "NULL", 30, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("Failed to cache movie detail", e);
            }
        }
        return ServiceResult.fail(ErrorCode.MOVIE_NOT_FOUND);
    }

    private boolean tryLock(String key, long expireTime, TimeUnit timeUnit) {
        try {
            return redisService.setValueWithExpireTimeIfAbsent(key, "LOCKED", expireTime, timeUnit);
        } catch (Exception e) {
            log.error("Failed to acquire lock for key: {}", key, e);
            return false;
        }
    }

    @Override
    public ServiceResult getWithPaging(String keyword) {
        try {
            PageResult<MovieDocument> pageResult = movieSearchService.search(keyword, null, null, null, PageRequest.of(0, 20));
            List<Long> ids = pageResult.getContent()
                    .stream().map(MovieDocument::getId)
                    .toList();

            prefixSearchSuggestionService.recordSearch(CommonConstants.RedisKey.PREFIX_SUGGESTION, keyword);

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

    @Override
    public ServiceResult suggestions(String keyword, Integer limit) {
        Set<String> suggestions = prefixSearchSuggestionService.suggest(CommonConstants.RedisKey.PREFIX_SUGGESTION, keyword, limit);
        return ServiceResult.ok(suggestions);
    }
}
