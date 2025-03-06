package org.example.webfluxmongo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.webfluxmongo.model.MovieInfo;
import org.example.webfluxmongo.model.MtTest;
import org.example.webfluxmongo.model.MtTestLombox;
import org.example.webfluxmongo.service.MovieInfoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@RestController
@RequestMapping("/flux-mono")
@Slf4j
//@AllArgsConstructor
public class FluxMonoController {

    private MovieInfoService movieInfoService;
    Sinks.Many<MovieInfo> movieInfoSink = Sinks.many().replay().latest();

    FluxMonoController(MovieInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
    }

    @GetMapping
    public Mono<Integer> helloMono() {
        return Mono.just(1).log();
    }

    @GetMapping("/flux/v1")
    public Flux<Integer> helloFlux() {
        return Flux.just(1, 2, 3).log();
    }

    @GetMapping(value = "/flux/v2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> helloFlux_v2(){
        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/flux/v3", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> helloFlux_v3(){
        return Flux.just(1,2,3,4,5,6,7,8,9,10)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/flux/v4", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> helloFlux_v4(){
        return Flux.interval(Duration.ofSeconds(5))
                .map(val -> getRandomNumber())
                .take(20)
                .log();
    }

    @GetMapping(value = "/flux/v5", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> helloFlux_v5(){
        return Flux.interval(Duration.ofSeconds(5))
                .flatMap(val -> movieInfoService.getMovieInfo("12345") // Fetch the movie info
                        .map(MovieInfo::getYear)) // Extract the year from MovieInfo
                .take(20) // Limit to 20 items
                .log(); // Log the data
    }

    private Integer getRandomNumber() {
        return (int) (Math.random() * 100);
    }

    @PostMapping("/addMovieInfo")
    public Mono<MovieInfo> addMovieInfo(@RequestBody MovieInfo movieInfo) {
        //log.info("addMovieInfo : {} " , movieInfo );
        return movieInfoService.addMovieInfo(movieInfo);
    }

    @GetMapping("/getMovieInfo/{movieId}")
    public Mono<MovieInfo> getMovieInfo(@PathVariable String movieId) {
        return movieInfoService.getMovieInfo(movieId);
    }

    @GetMapping("/getAllMovieInfo")
    public Flux<MovieInfo> getAllMovieInfo() {
        return movieInfoService.getAllMovieInfo();
    }

    @GetMapping(value = "/getAllMovieInfo/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MovieInfo> getAllMovieInfo_stream() {
        return movieInfoService.getAllMovieInfo()
                .delayElements(Duration.ofSeconds(5));
    }

    @PostMapping("/addMttest")
    public Mono<MtTest> addMtTest(@RequestBody MtTest mtTest) {
        return Mono.just(mtTest);
    }

    @PostMapping("/addMttestlom")
    public Mono<MtTestLombox> addMtTest(@RequestBody MtTestLombox mtTest) {

        return Mono.just(mtTest);
    }

    @GetMapping("/learnmap")
    public void getMaplearntest() {
        getMaplearn().subscribe(item -> System.out.println("Received: " + item), // OnNext
                error -> System.err.println("Error: " + error),  // OnError
                () -> System.out.println("Done!"));         // OnComplete);
    }

    public Flux<Integer> getMaplearn() {
        return Flux.just(1,2,3);
    }

    @GetMapping("chars/v1")
    public void getcharslearn() {
       String name = "kannan";

       name.chars()
               .mapToObj(s -> (char) s)
               .forEach(System.out::println);

    }

    @GetMapping("flatmap/v1")
    public Flux<String> getFlatMaplearn2() {
        Flux<String> a =  Flux.just("Alex", "kannan", "sathya");

        return a.flatMap(s -> splitstring(s));

    }

    @GetMapping("flatmap/v2")
    public Flux<String> getFlatMaplearn3() {
        Flux<String> a =  Flux.just("Alex", "kannan", "sathya");

        return a.flatMap(s -> Flux.fromArray(s.split("")));

    }

    private Flux<String> splitstring(String name) {
        var charArray =  name.split("");

        return Flux.fromArray(charArray);
    }

    @GetMapping("concatmap/v1")
    public Flux<String> getConcatMaplearn1() {
        Flux<String> a =  Flux.just("Alex", "kannan", "sathya");

        return a.concatMap(s -> Flux.fromArray(s.split("")));

    }

    @GetMapping("concat/v1")
    public Flux<String> getConcatlearn1() {
        Flux<String> a =  Flux.just("Alex", "kannan", "sathya");
        Flux<String> b =  Flux.just("Alex1", "kannan1", "sathya1");

        //return Flux.concat(a, b);
        //return a.concatWith(b); //sequence, publishers subscribed one after another

        //return Flux.merge(a, b); // both the publishers subscribed at same time so sequence not guaranted.
          return Flux.mergeSequential(a, b);
    }

    @GetMapping(value = "concat/v2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getConcatlearn2() {
        Flux<String> a =  Flux.interval(Duration.ofSeconds(3))
                .map(val -> "Temperature -- " + (20 + val) ).take(5);

        Flux<String> b = Flux.interval(Duration.ofSeconds(5))
                .map(s -> "Pressure -- " + (30+s)).take(5);

        //return Flux.concat(a, b); //sequence, publishers subscribed one after another
        //return Flux.merge(a, b); // both the publishers subscribed at same time so sequence not guaranted.

        //Flux<String> rest = Flux.mergeSequential(a, b);
        //rest.subscribe(System.out::println);
        return Flux.mergeSequential(a, b); // both the publishers subscribed at same time, but it will sequence guaranted.

    }

    @GetMapping(value = "zip/v1", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getziplearn1() {
        Flux<String> a =  Flux.interval(Duration.ofSeconds(3))
                .map(val -> "Temperature -- " + (20 + val) ).take(5);

        Flux<String> b = Flux.interval(Duration.ofSeconds(5))
                .map(s -> "Pressure -- " + (30+s)).take(5);

        return Flux.zip(a,b, (i1,i2) -> i1+i2);

    }

    @GetMapping(value = "/backpressure/v1", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> backpressure_streamData_V1() {
        // Simulate a fast data producer emitting items every 10ms, kafka,DB,External API
        Flux<String> fastProducer = Flux.interval(Duration.ofMillis(10))
                .map(i -> "Item " + i)
                .take(10); // Limit the stream to 100 items

        // Apply backpressure using onBackpressureBuffer to buffer items
        return fastProducer
                .onBackpressureBuffer(50,
                        item -> System.out.println("Dropped: " + item), // Handle dropped items
                        BufferOverflowStrategy.DROP_OLDEST) // Specify backpressure strategy
                .doOnNext(item -> {
                    // Simulate slow consumer processing time
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .subscribeOn(Schedulers.boundedElastic()) // Use an elastic scheduler for parallelism
                .log();
    }

    @GetMapping(value = "/backpressure/v2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> backpressure_streamData_V2() {
        // Simulate a fast data producer emitting items every 10ms, kafka,DB,External API
        Flux<String> fastProducer = Flux.interval(Duration.ofMillis(10))
                .map(i -> "Item " + i);
        //.take(100); // Limit the stream to 100 items

        // Apply backpressure using onBackpressureBuffer to buffer items
        return fastProducer
                .onBackpressureBuffer(50,
                        item -> System.out.println("Dropped: " + item), // Handle dropped items
                        BufferOverflowStrategy.DROP_OLDEST) // Specify backpressure strategy
                .doOnNext(item -> {
                    // Simulate slow consumer processing time
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .subscribeOn(Schedulers.boundedElastic()) // Use an elastic scheduler for parallelism
                .log();
    }

    /*

    Flux<Integer> generatedFlux = Flux.generate(
    sink -> {
        sink.next((int) (Math.random() * 100));
        sink.complete();
    });

    Flux<Integer> createdFlux = Flux.create(emitter -> {
    for (int i = 1; i <= 5; i++) {
        emitter.next(i);
    }
    emitter.complete();
});


     */
}
