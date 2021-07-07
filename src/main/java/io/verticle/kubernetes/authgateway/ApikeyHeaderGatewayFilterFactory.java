package io.verticle.kubernetes.authgateway;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.verticle.kubernetes.authgateway.apikeys.KeyService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Hints;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class ApikeyHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory<ApikeyHeaderGatewayFilterFactory.Config> {

    public static final String APIKEY = "x-authgw-apikey";
    Log log = LogFactory.getLog(this.getClass());

    @Autowired
    KeyService keyService;

    @Autowired
    KubernetesClient kubernetesClient;

    public ApikeyHeaderGatewayFilterFactory() {
        super(Config.class);
    }

    private String getNamespace(){
        return kubernetesClient.getNamespace();
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            log.info("ApikeyHeader " + exchange.getRequest().getId());

            boolean apiKeyMatches = false;
            String apiKey = exchange.getRequest().getHeaders().getFirst(APIKEY);
            apiKeyMatches = keyService.match(apiKey, getNamespace());
            log.info("apikey: " + apiKey + "in NS " + getNamespace() + " matches: " + apiKeyMatches);

            if (!apiKeyMatches || apiKey == null) {
                log.info("Access denied");

                ServerWebExchangeUtils.setResponseStatus(exchange, HttpStatus.UNAUTHORIZED);
                ServerWebExchangeUtils.setAlreadyRouted(exchange);
                final Map<String, String> error = Map.of("error", "unauthorized");
                return chain.filter(exchange).then(Mono.defer(() -> {
                    final ServerHttpResponse response = exchange.getResponse();
                    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8.toString());
                    return response.writeWith(new Jackson2JsonEncoder().encode(Mono.just(error),
                            response.bufferFactory(),
                            ResolvableType.forInstance(error),
                            MediaType.APPLICATION_JSON_UTF8,
                            Hints.from(Hints.LOG_PREFIX_HINT, exchange.getLogPrefix()))
                    );
                }));

            }

            ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
            exchange.mutate().request(builder.build()).build();

            return chain.filter(exchange);
        };
    }

    public static class Config {
    }

}