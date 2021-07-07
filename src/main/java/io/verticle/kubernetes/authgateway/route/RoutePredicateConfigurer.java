package io.verticle.kubernetes.authgateway.route;

import io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute.HTTPRouteFilterSpec;
import io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute.HTTPRouteForwardToSpec;
import io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute.HTTPRouteRuleSpec;
import io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute.HostnameSpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.handler.predicate.*;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class RoutePredicateConfigurer {

    Log log = LogFactory.getLog(this.getClass());

    @Autowired
    HostRoutePredicateFactory hostRoutePredicateFactory;

    @Autowired
    PathRoutePredicateFactory pathRoutePredicateFactory;

    @Autowired
    HeaderRoutePredicateFactory headerRoutePredicateFactory;

    @Autowired
    MethodRoutePredicateFactory methodRoutePredicateFactory;

    @Autowired
    QueryRoutePredicateFactory queryRoutePredicateFactory;

    @Autowired
    RemoteAddrRoutePredicateFactory remoteAddrRoutePredicateFactory;

    @Autowired
    CookieRoutePredicateFactory cookieRoutePredicateFactory;

    @Autowired
    AfterRoutePredicateFactory afterRoutePredicateFactory;

    @Autowired
    BeforeRoutePredicateFactory beforeRoutePredicateFactory;

    @Autowired
    BetweenRoutePredicateFactory betweenRoutePredicateFactory;

    @Autowired
    WeightRoutePredicateFactory weightRoutePredicateFactory;


    public RoutePredicateConfigurer() {
    }

    protected void applyHostPredicate(PredicateSpec r, AtomicReference<BooleanSpec> b, HostnameSpec hostnameSpec) {

        if (hostnameSpec.size() > 0){
            b.get().and().predicate(hostRoutePredicateFactory.apply(config -> {
                List<String> patterns = new ArrayList<String>();
                hostnameSpec.forEach(
                        host -> {
                            patterns.add(host);
                        }
                );
                config.setPatterns(patterns);
            }));
        }
    }

    /**
     * implements https://gateway-api.sigs.k8s.io/references/spec/#networking.x-k8s.io/v1alpha1.HTTPPathMatch
     * @param r
     * @param b
     * @param httpRouteRuleSpec
     */
    protected void applyPathPredicate(PredicateSpec r, AtomicReference<BooleanSpec> b, HTTPRouteRuleSpec httpRouteRuleSpec){

        if (httpRouteRuleSpec.getMatches().size() > 0){
            b.get().and().predicate(pathRoutePredicateFactory.apply(config -> {

                List<String> patterns = new ArrayList<>();
                httpRouteRuleSpec.getMatches().forEach(
                        httpRouteMatchSpec -> {
                            patterns.add(httpRouteMatchSpec.getPath().getValue());
                        }
                );
                config.setPatterns(patterns);
                // TODO config.setMatchTrailingSlash(httpRouteMatchSpec);
            }));
        }
    }

    /**
     * adds a weight predicate
     * implements Custom filter
     * https://gateway-api.sigs.k8s.io/references/spec/#networking.x-k8s.io/v1alpha1.HTTPRouteFilter
     * @param r
     * @param b
     * @param httpRouteForwardToSpec
     */
    protected void applyWeightPredicate(PredicateSpec r, AtomicReference<BooleanSpec> b, HTTPRouteForwardToSpec httpRouteForwardToSpec){

        if (httpRouteForwardToSpec.getWeight() > 0){
            b.get().and().predicate(weightRoutePredicateFactory.apply(config -> {
                config.setWeight(httpRouteForwardToSpec.getWeight());
                config.setRouteId("weight_" + httpRouteForwardToSpec.getServiceName() + "_" + httpRouteForwardToSpec.getWeight());
                config.setGroup("weight_" + httpRouteForwardToSpec.getServiceName());
            }));
        }

    }

    /**
     * implements Custom filter
     * https://gateway-api.sigs.k8s.io/references/spec/#networking.x-k8s.io/v1alpha1.HTTPRouteFilter
     * @param r
     * @param b
     */
    protected void applyCookiePredicate(PredicateSpec r, AtomicReference<BooleanSpec> b){
        b.get().and().predicate(cookieRoutePredicateFactory.apply(config -> {
            // TODO implement
        }));
    }

    /**
     * implements Custom filter
     * https://gateway-api.sigs.k8s.io/references/spec/#networking.x-k8s.io/v1alpha1.HTTPRouteFilter
     * @param r
     * @param b
     */
    protected void applyAfterPredicate(PredicateSpec r, AtomicReference<BooleanSpec> b){
        b.get().and().predicate(afterRoutePredicateFactory.apply(config -> {
            // TODO implement
        }));
    }

    /**
     * implements Custom filter
     * https://gateway-api.sigs.k8s.io/references/spec/#networking.x-k8s.io/v1alpha1.HTTPRouteFilter
     * @param r
     * @param b
     */
    protected void applyBeforePredicate(PredicateSpec r, AtomicReference<BooleanSpec> b){
        b.get().and().predicate(beforeRoutePredicateFactory.apply(config -> {
            // TODO implement
        }));
    }

    /**
     * implements Custom filter
     * https://gateway-api.sigs.k8s.io/references/spec/#networking.x-k8s.io/v1alpha1.HTTPRouteFilter
     * @param r
     * @param b
     */
    protected void applyBetweenPredicate(PredicateSpec r, AtomicReference<BooleanSpec> b){
        b.get().and().predicate(betweenRoutePredicateFactory.apply(config -> {
            // TODO implement
        }));
    }

    /**
     * implements Custom filter
     * https://gateway-api.sigs.k8s.io/references/spec/#networking.x-k8s.io/v1alpha1.HTTPRouteFilter
     * @param r
     * @param b
     */
    protected void applyMethodPredicate(PredicateSpec r, AtomicReference<BooleanSpec> b){
        b.get().and().predicate(methodRoutePredicateFactory.apply(config -> {
            // TODO implement
        }));
    }

    /**
     * implements https://gateway-api.sigs.k8s.io/references/spec/#networking.x-k8s.io/v1alpha1.HTTPQueryParamMatch
     * @param r
     * @param b
     */
    protected void applyQueryPredicate(PredicateSpec r, AtomicReference<BooleanSpec> b){
        b.get().and().predicate(queryRoutePredicateFactory.apply(config -> {
            // TODO implement
        }));
    }

    /**
     * implements https://gateway-api.sigs.k8s.io/references/spec/#networking.x-k8s.io/v1alpha1.HTTPHeaderMatch
     * @param r
     * @param b
     * @param f
     */
    protected void applyHeaderPredicate(PredicateSpec r, AtomicReference<BooleanSpec> b, HTTPRouteFilterSpec f){
        b.get().and().predicate(queryRoutePredicateFactory.apply(config -> {
            // TODO implement
        }));
    }

    /**
     * implements Custom filter
     * https://gateway-api.sigs.k8s.io/references/spec/#networking.x-k8s.io/v1alpha1.HTTPRouteFilter
     * @param r
     * @param b
     */
    protected void applyRemoteAddrPredicate(PredicateSpec r, AtomicReference<BooleanSpec> b){
        b.get().and().predicate(remoteAddrRoutePredicateFactory.apply(config -> {
            // TODO implement
        }));
    }
}