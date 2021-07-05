package io.verticle.kubernetes.authgateway.route;

import io.verticle.kubernetes.authgateway.ApikeyHeaderGatewayFilterFactory;
import io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute.HTTPRoute;
import io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute.HTTPRouteRuleListSpec;
import io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute.HostnameSpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class RouteConfigService implements ApplicationEventPublisherAware, ApplicationContextAware {

    Log log = LogFactory.getLog(this.getClass());

    @Autowired
    RuntimeRouteLocator runtimeRouteLocator;

    @Autowired
    RouteLocator locator;

    @Autowired
    RouteLocatorBuilder builder;

    @Autowired
    ApikeyHeaderGatewayFilterFactory apikeyHeaderGateway;

    @Autowired
    RouteDefinitionWriter routeDefinitionWriter;


    ApplicationEventPublisher publisher;
    private ApplicationContext applicationContext;


    public void mapCustomResource(HTTPRoute resource) {

        log.info(resource);

        HTTPRouteRuleListSpec rulesList = resource.getSpec().getRules();
        HostnameSpec hostnameSpec = resource.getSpec().getHostnames();


        Flux<Route> fluxRoutes = builder.routes().route(resource.getMetadata().getName(), r -> {


            AtomicReference<Buildable<Route>> route = new AtomicReference<>();
            rulesList.forEach(

                    httpRouteRuleSpec -> {


                        AtomicReference<BooleanSpec> b = new AtomicReference<>();

                        // match paths
                        httpRouteRuleSpec.getMatches().forEach(
                                httpRouteMatchSpec -> {
                                    b.set(r.path(httpRouteMatchSpec.getPath().getValue()));
                                }
                        );

                        // match hostnames
                        hostnameSpec.forEach(hostname -> {
                            b.get().and().host(hostname);
                        });

                        // apply filters
                        b.get().filters(f -> f.filter(apikeyHeaderGateway.apply(config -> {

                        })));


                        httpRouteRuleSpec.getForwardTo().forEach(
                                httpRouteForwardToSpec -> {

                                    URI uri = new DefaultUriBuilderFactory().builder()
                                            .scheme("http")
                                            .host(httpRouteForwardToSpec.getServiceName())
                                            .port(httpRouteForwardToSpec.getPort())
                                            .build();
                                    //ObjectUtils.defaultIfNull(httpRouteForwardToSpec.getWeight(), 100);
                                    route.set(b.get().uri(uri));
                                });

                    });
            return route.get();

        }).build().getRoutes();
        runtimeRouteLocator.setRoutes(fluxRoutes);

        // update routing
        this.publisher.publishEvent(new RefreshRoutesEvent(this));


    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
