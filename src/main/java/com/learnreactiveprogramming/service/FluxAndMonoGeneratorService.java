package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

@Slf4j
public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .log(); // db or a remote service call
    }

    public Mono<String> nameMono() {

        return Mono.just("alex");
    }

    public Flux<String> namesFlux_map(int stringLength) {
        //filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                //.map(s -> s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                .map(s -> s.length() + "-" + s)//4-ALEX, 5-CHLOE
                .doOnNext(name -> {
                    System.out.println("Name is : " + name);
                    name.toLowerCase();
                })
                .doOnSubscribe(s -> {
                    System.out.println("Subscription is : " + s);
                })
                .doOnComplete(() -> {
                    System.out.println("Inside the complete callback");
                })
                .doFinally(signalType -> {
                    System.out.println("inside dofinally : " + signalType);
                })
                .log(); // db or a remote service call
    }

    public Flux<String> namesFlux_immutability() {

        var namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));
        namesFlux.map(String::toUpperCase);
        return namesFlux;
    }

    public Mono<String> namesMono_map_filter(int stirngLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stirngLength);

    }

    public Mono<List<String>> namesMono_flatMap(int stirngLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stirngLength)
                .flatMap(this::splitStringMono)
                .log();  //Mono<List of A, L, E  X>

    }

    public Flux<String> namesMono_flatMapMany(int stirngLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stirngLength)
                .flatMapMany(this::splitString)
                .log();  //Mono<List of A, L, E  X>

    }

    private Mono<List<String>> splitStringMono(String s) {
        var charArray = s.split("");
        var charList = List.of(charArray); //ALEX -> A, L, E, X
        return Mono.just(charList);
    }

    public Flux<String> namesFlux_flatmap(int stringLength) {
        //filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                //.map(s -> s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                // ALEX,CHLOE ->A, L, E, X, C, H, L , O, E
                .flatMap(s -> splitString(s)) // A,L,E,X,C,H,L,O,E
                .log(); // db or a remote service call
    }

    public Flux<String> namesFlux_flatmap_async(int stringLength) {
        //filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                //.map(s -> s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                // ALEX,CHLOE ->A, L, E, X, C, H, L , O, E
                .flatMap(s -> splitString_withDelay(s)) // A,L,E,X,C,H,L,O,E
                .log(); // db or a remote service call
    }

    public Flux<String> namesFlux_concatmap(int stringLength) {
        //filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                //.map(s -> s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                // ALEX,CHLOE ->A, L, E, X, C, H, L , O, E
                .concatMap(s -> splitString_withDelay(s)) // A,L,E,X,C,H,L,O,E
                .log(); // db or a remote service call
    }

    public Flux<String> namesFlux_transform(int stringLength) {
        //filter the string whose length is greater than 3

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);

        //Flux.empty()
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .flatMap(s -> splitString(s)) // A,L,E,X,C,H,L,O,E
                .defaultIfEmpty("default")
                .log(); // db or a remote service call
    }

    public Flux<String> namesFlux_transform_switchifEmpty(int stringLength) {
        //filter the string whose length is greater than 3

        Function<Flux<String>, Flux<String>> filterMap = name ->
                name.map(String::toUpperCase)
                        .filter(s -> s.length() > stringLength)
                        .flatMap(s -> splitString(s));

        var defaultFlux = Flux.just("default")
                .transform(filterMap); //"D","E","F","A","U","L","T"
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                // A,L,E,X,C,H,L,O,E
                .switchIfEmpty(defaultFlux)
                .log(); // db or a remote service call
    }

    public Flux<String> explore_concat() {

        var abcFlux = Flux.just("A", "B", "C");

        var defFlux = Flux.just("D", "E", "F");

        return Flux.concat(abcFlux, defFlux);

    }

    public Flux<String> explore_concatwith() {

        var abcFlux = Flux.just("A", "B", "C");

        var defFlux = Flux.just("D", "E", "F");

        return abcFlux.concatWith(defFlux);

    }

    public Flux<String> explore_concatwith_mono() {

        var aMono = Mono.just("A");

        var bMono = Mono.just("B");

        return aMono.concatWith(bMono);

    }

    public Flux<String> explore_merge() {

        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100)); //A,B

        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));//D,E

        return Flux.merge(abcFlux, defFlux).log();

    }

    public Flux<String> explore_mergeWith() {

        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100)); //A,B

        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));//D,E

        //return Flux.merge(abcFlux,defFlux).log();
        return abcFlux.mergeWith(defFlux).log();

    }

    public Flux<String> explore_mergeWith_mono() {

        var aMono = Mono.just("A"); //A

        var bMono = Mono.just("B"); //B

        return aMono.mergeWith(bMono).log(); // A, B

    }

    public Flux<String> explore_mergeSequential() {

        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100)); //A,B

        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));//D,E

        return Flux.mergeSequential(abcFlux, defFlux).log();
        // return abcFlux.mergeWith(defFlux).log();

    }

    public Flux<String> explore_zip() {

        var abcFlux = Flux.just("A", "B", "C");

        var defFlux = Flux.just("D", "E", "F");

        return Flux.zip(abcFlux, defFlux, (first, second) -> first + second)
                .log(); //AD, BE, CF

    }

    public Flux<String> explore_zip_1() {

        var abcFlux = Flux.just("A", "B", "C");

        var defFlux = Flux.just("D", "E", "F");

        var _123Flux = Flux.just("1", "2", "3");
        var _456Flux = Flux.just("4", "5", "6");

        return Flux.zip(abcFlux, defFlux, _123Flux, _456Flux)
                .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4())
                .log(); //AD14, BE25, CF36

    }

    public Flux<String> explore_zipWith() {

        var abcFlux = Flux.just("A", "B", "C");

        var defFlux = Flux.just("D", "E", "F");

        return abcFlux.zipWith(defFlux, (first, second) -> first + second)
                .log(); //AD, BE, CF

    }

    public Mono<String> explore_ZipWith_mono() {

        var aMono = Mono.just("A"); //A

        var bMono = Mono.just("B"); //B

        return aMono.zipWith(bMono)
                .map(t2 -> t2.getT1() + t2.getT2()) //AB
                .log(); // A, B

    }

    public Flux<String> exception_flux() {

        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .log();
    }

    public Flux<String> explore_OnErrorReturn() {
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new IllegalStateException("Exception Occurred")))
                .onErrorReturn("D")
                .log();
    }

    public Flux<String> explore_OnErrorResume(Exception e) {

        var recoveryFlux =  Flux.just("D", "E", "F");

        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(e))
                .onErrorResume(ex -> {
                    log.error("Exception is ", ex);
                    if(ex instanceof IllegalStateException)
                        return recoveryFlux;
                    else
                        return Flux.error(ex);
                })
                .log();
    }

    public Flux<String> explore_OnErrorContinue() {

        return Flux.just("A", "B", "C")
                .map(name -> {
                    if(name.equals("B"))
                        throw new IllegalStateException("Exception Occurred");
                    return name;
                })
                .concatWith(Flux.just("D"))
                .onErrorContinue((ex,name) -> {
                    log.error("Exception is ", ex);
                    log.info("name is {}", name);
                })
                .log();
    }

    public Flux<String> explore_OnErrorMap() {

        return Flux.just("A", "B", "C")
                .map(name -> {
                    if(name.equals("B"))
                        throw new IllegalStateException("Exception Occurred");
                    return name;
                })
                .concatWith(Flux.just("D"))
                .onErrorMap((ex) -> {
                    log.error("Exception is ", ex);
                    return new ReactorException(ex, ex.getMessage());
                })
                .log();
    }

    public Flux<String> explore_doOnError() {
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new IllegalStateException("Exception Occurred")))
                .doOnError(ex->{
                    log.error("Exception is ", ex);
                })
                .log();
    }

    public Mono<Object> explore_Mono_OnErrorReturn() {
        return Mono.just("A")
                .map(value ->{
                    throw new RuntimeException("Exception Occurred");
                })
                .onErrorReturn("abc")
                .log();
    }


    //ALEX -> FLux(A,L,E,X)
    public Flux<String> splitString(String name) {
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> splitString_withDelay(String name) {
        var charArray = name.split("");
        // var delay = new Random().nextInt(1000);
        var delay = 1000;
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
    }


    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> {
                    System.out.println("Name is : " + name);
                });

        fluxAndMonoGeneratorService.nameMono()
                .subscribe(name -> {
                    System.out.println("Mono name is : " + name);
                });

    }
}
