package org.example.webfluxmongo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fmeg")
public class FluxMonoTransformController {

    @GetMapping("/mono/v1")
    public Mono<Integer> transform_mono_v1() {
        return Mono.just(10);
    }

    @GetMapping("/mono/v2")
    public Mono<Integer> transform_mono_v2() {
        Mono<Integer> myval =  Mono.just(10);

        return myval.map(integer -> integer + 10);
    }

    @GetMapping("/flux/v1")
    public Flux<Integer> transform_flux_v1() {
        return Flux.just(1,2,3);
    }

    @GetMapping("/flux/v2")
    public Flux<Integer> transform_flux_v2() {
        Flux<Integer> myval =  Flux.just(1,2,3);

        //return myval.map(integer -> integer + 10);

        return myval.flatMap(val -> Flux.just(val + 5));
    }

    @GetMapping("/flux/v3")
    public Flux<Integer> transform_flux_v3() {
        Flux<Integer> myval =  Flux.just(1,2,3);

        //return myval.map(integer -> integer + 10);

        return myval.flatMap(val ->
                {
                    if (val % 2 != 0) {
                        return Flux.just(val + 5);
                    }  else {
                        return Flux.empty(); // Ignore odd values
                    }
                });
    }

    @GetMapping("/mono/v3")
    public Mono<Integer> transform_mono_v3() {
        Flux<Integer> myval =  Flux.just(1,2,3);

        //return myval.reduce(0, Integer::sum); // Adds all values

        //collectList() converts Flux<Integer> to Mono<List<Integer>>.
        //flatMap then calculates the sum.
        return myval.collectList().flatMap(list -> Mono.just(list.stream().mapToInt(Integer::intValue).sum()));

    }

    @GetMapping("/flux/v4")
    public Flux<Integer> transform_flux_v4() {
        Mono<Integer> myval =  Mono.just(3);

        return myval.flatMapMany(integer -> Flux.just(integer + 5, integer + 10));
    }
}
