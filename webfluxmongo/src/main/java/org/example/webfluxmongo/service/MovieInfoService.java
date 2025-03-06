package org.example.webfluxmongo.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.webfluxmongo.Repository.MovieInfoRepository;
import org.example.webfluxmongo.model.MovieInfo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
//@AllArgsConstructor
public class MovieInfoService {

    private MovieInfoRepository movieInfoRepository;

    MovieInfoService(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
        //log.info("addMovieInfo : {} " , movieInfo );
        return movieInfoRepository.save(movieInfo);
    }

    public Mono<MovieInfo> getMovieInfo(String movieId) {
        return movieInfoRepository.findById(movieId);
    }

    public Flux<MovieInfo> getAllMovieInfo() {
        return movieInfoRepository.findAll();
    }
}
