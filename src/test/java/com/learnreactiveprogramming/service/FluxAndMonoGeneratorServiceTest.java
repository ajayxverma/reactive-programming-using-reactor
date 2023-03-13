package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

public class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService =
            new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        //given

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        //then
        StepVerifier.create(namesFlux)
                //.expectNext("alex", "ben","chloe")
                //.expectNextCount(3)
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void namesFlux_map() {

        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_map(stringLength);

        //then
        StepVerifier.create(namesFlux)
                //.expectNext("ALEX", "BEN", "CHLOE")
                .expectNext("4-ALEX","5-CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFlux_immutability() {
        //given

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_immutability();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("alex", "ben","chloe")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatmap() {
        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap(stringLength);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A","L","E","X","C","H","L","O","E")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatmap_async() {
        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap_async(stringLength);

        //then
        StepVerifier.create(namesFlux)
                //.expectNext("A","L","E","X","C","H","L","O","E")
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_concatmap() {
        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_concatmap(stringLength);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A","L","E","X","C","H","L","O","E")
                //.expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_transform() {
        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A","L","E","X","C","H","L","O","E")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_1() {
        //given
        int stringLength = 6;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        //then
        StepVerifier.create(namesFlux)
                //.expectNext("A","L","E","X","C","H","L","O","E")
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_switchifEmpty() {
        //given
        int stringLength = 6;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform_switchifEmpty(stringLength);

        //then
        StepVerifier.create(namesFlux)
                //.expectNext("A","L","E","X","C","H","L","O","E")
                .expectNext("D","E","F","A","U","L","T")
                .verifyComplete();
    }

    @Test
    void explore_concat() {
        //given

        //when
        var concatFlux = fluxAndMonoGeneratorService.explore_concat();

        //then
        StepVerifier.create(concatFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();

    }

    @Test
    void explore_merge() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_merge();

        //then
        StepVerifier.create(value)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void explore_mergeSequential() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_mergeSequential();

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void explore_zip() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_zip();

        //then
        StepVerifier.create(value)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }

    @Test
    void explore_zip_1() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_zip_1();

        //then
        StepVerifier.create(value)
                .expectNext("AD14","BE25","CF36")
                .verifyComplete();
    }

    @Test
    void namesMono_flatMap() {
        //given
        int stringLength = 3;

        //when
        var value = fluxAndMonoGeneratorService.namesMono_flatMap(stringLength);

        //then
        StepVerifier.create(value)
                .expectNext(List.of("A","L","E","X"))
                .verifyComplete();
    }

    @Test
    void namesMono_flatMapMany() {
        //given
        int stringLength = 3;

        //when
        var value = fluxAndMonoGeneratorService.namesMono_flatMapMany(stringLength);

        //then
        StepVerifier.create(value)
                .expectNext("A","L","E","X")
                .verifyComplete();
    }

    @Test
    void exception_flux() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.exception_flux();

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void exception_flux_1() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.exception_flux();

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectError()
                .verify();
    }
    @Test
    void exception_flux_2() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.exception_flux();

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectErrorMessage("Exception Occurred")
                .verify();
    }

    @Test
    void explore_OnErrorReturn() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_OnErrorReturn();

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C","D")
                .verifyComplete();
    }

    @Test
    void explore_OnErrorResume() {
        //given
        var e = new IllegalStateException("Not a valid State");

        //when
        var value = fluxAndMonoGeneratorService.explore_OnErrorResume(e);

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C","D","E", "F")
                .verifyComplete();
    }

    @Test
    void explore_OnErrorResume_1() {
        //given
        var e = new RuntimeException("Not a valid State");

        //when
        var value = fluxAndMonoGeneratorService.explore_OnErrorResume(e);

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void explore_OnErrorContinue() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_OnErrorContinue();

        //then
        StepVerifier.create(value)
                .expectNext("A","C","D")
                .verifyComplete();
    }

    @Test
    void explore_OnErrorMap() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_OnErrorMap();

        //then
        StepVerifier.create(value)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void explore_doOnError() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_doOnError();

        //then
        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectError(IllegalStateException.class)
                .verify();

    }

    @Test
    void explore_Mono_OnErrorReturn() {
        //given

        //when
        var value = fluxAndMonoGeneratorService.explore_Mono_OnErrorReturn();

        //then
        StepVerifier.create(value)
                .expectNext("abc")
                .verifyComplete();

    }
}
