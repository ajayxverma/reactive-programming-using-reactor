package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class MovieReactiveServiceTest {

    private MovieInfoService movieInfoService =new MovieInfoService();
    private ReviewService reviewService
            =new ReviewService();
    private RevenueService revenueService
            =new RevenueService();
    MovieReactiveService movieReactiveService
            = new MovieReactiveService(movieInfoService,reviewService,revenueService);


    @Test
    void getAllMovies() {
        //given

        //when
        var moviesFlux = movieReactiveService.getAllMovies();

        //then
        StepVerifier.create(moviesFlux)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovieInfo().getName());
                    assertEquals(2, movie.getReviewList().size());
                    //name of the movie
                    //reviewsList
                })
                .assertNext(movie -> {
                    assertEquals("The Dark Knight", movie.getMovieInfo().getName());
                    assertEquals(2, movie.getReviewList().size());
                    //name of the movie
                    //reviewsList
                })
                .assertNext(movie -> {
                    assertEquals("Dark Knight Rises", movie.getMovieInfo().getName());
                    assertEquals(2, movie.getReviewList().size());
                    //name of the movie
                    //reviewsList
                })
                .verifyComplete();
    }

    @Test
    void getMovieById() {


        //given
        long movieId = 100L;

        //when
        var movieMono = movieReactiveService.getMovieById(movieId).log();

        //then
        StepVerifier.create(movieMono)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovieInfo().getName());
                    assertEquals(2, movie.getReviewList().size());
                    //name of the movie
                    //reviewsList
                })
                .verifyComplete();
    }

    @Test
    void getMovieById_withRevenue() {
        //given
        long movieId = 100L;

        //when
        var movieMono = movieReactiveService.getMovieById_withRevenue(movieId).log();

        //then
        StepVerifier.create(movieMono)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovieInfo().getName());
                    assertEquals(2, movie.getReviewList().size());
                    //name of the movie
                    //reviewsList
                    assertNotNull(movie.getRevenue());
                })
                .verifyComplete();
    }
}