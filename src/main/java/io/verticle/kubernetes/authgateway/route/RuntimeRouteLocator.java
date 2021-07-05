package io.verticle.kubernetes.authgateway.route;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class RuntimeRouteLocator implements RouteLocator {

    private Flux<Route> routes = Flux.empty();

    @Override
    public Flux<Route> getRoutes() {
        return routes;
    }


    public void setRoutes(Flux<Route> routes) {
        this.routes = routes;
    }
}
