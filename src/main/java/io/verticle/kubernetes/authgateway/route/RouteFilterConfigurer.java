package io.verticle.kubernetes.authgateway.route;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.verticle.kubernetes.authgateway.ApikeyHeaderGatewayFilterFactory;
import io.verticle.kubernetes.authgateway.GatewayClass;
import io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute.HTTPRequestHeaderFilterSpec;
import io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute.LocalObjectReference;
import org.apache.commons.lang3.ObjectUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.*;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class RouteFilterConfigurer {

    @Autowired
    GatewayClass gatewayClass;

    enum SCGFilters {
        AddRequestHeader,
        AddRequestParameter,
        AddResponseHeader,
        DedupeResponseHeader,
        CircuitBreaker,
        FallbackHeaders,
        MapRequestHeader,
        PrefixPath,
        PreserveHost,
        RequestRateLimiter,
        RedirectTo,
        RemoveRequestHeader,
        RemoveResponseHeader,
        RemoveRequestParameter,
        RewritePath,
        RewriteLocationResponseHeader,
        RewriteResponseHeader,
        SaveSession,
        SecureHeaders,
        SetPath,
        SetRequestHeader,
        SetResponseHeader,
        SetStatus,
        StripPrefix,
        Retry,
        RequestSize,
        SetRequestHost

    }
    // ModifyResponseBody not supported yet
    // ModifyRequestBody not supported yet


    static final String FEATURE_APIKEY_ENABELED = "apikeyEnabled";

    @Autowired
    KubernetesClient kubernetesClient;

    // verticle filters

    @Autowired
    ApikeyHeaderGatewayFilterFactory apikeyHeaderGateway;

    // filters implementing Gateway API spec

    @Autowired
    AddRequestHeaderGatewayFilterFactory addRequestHeaderGatewayFilterFactory;

    @Autowired
    RemoveRequestHeaderGatewayFilterFactory removeRequestHeaderGatewayFilterFactory;

    @Autowired
    MapRequestHeaderGatewayFilterFactory mapRequestHeaderGatewayFilterFactory;


    // other custom filter from Spring Cloud Gateway

    @Autowired
    AddRequestParameterGatewayFilterFactory addRequestParameterGatewayFilterFactory;

    @Autowired
    AddResponseHeaderGatewayFilterFactory addResponseHeaderGatewayFilterFactory;

    @Autowired
    DedupeResponseHeaderGatewayFilterFactory dedupeResponseHeaderGatewayFilterFactory;

    //@Autowired
    //SpringCloudCircuitBreakerFilterFactory circuitBreakerGatewayFilterFactory;

    //@Autowired
    //FallbackHeadersGatewayFilterFactory fallbackHeadersGatewayFilterFactory;

    @Autowired
    PrefixPathGatewayFilterFactory prefixPathGatewayFilterFactory;

    @Autowired
    PreserveHostHeaderGatewayFilterFactory preserveHostHeaderGatewayFilterFactory;

    //@Autowired
    //RequestRateLimiterGatewayFilterFactory requestRateLimiterGatewayFilterFactory;

    @Autowired
    RedirectToGatewayFilterFactory redirectToGatewayFilterFactory;

    @Autowired
    RemoveResponseHeaderGatewayFilterFactory removeResponseHeaderGatewayFilterFactory;

    @Autowired
    RemoveRequestParameterGatewayFilterFactory removeRequestParameterGatewayFilterFactory;

    @Autowired
    RewritePathGatewayFilterFactory rewritePathGatewayFilterFactory;

    @Autowired
    RewriteLocationResponseHeaderGatewayFilterFactory rewriteLocationResponseHeaderGatewayFilterFactory;

    @Autowired
    RewriteResponseHeaderGatewayFilterFactory rewriteResponseHeaderGatewayFilterFactory;

    @Autowired
    SaveSessionGatewayFilterFactory saveSessionGatewayFilterFactory;

    @Autowired
    SecureHeadersGatewayFilterFactory secureHeadersGatewayFilterFactory;

    @Autowired
    SetPathGatewayFilterFactory setPathGatewayFilterFactory;

    @Autowired
    SetRequestHeaderGatewayFilterFactory setRequestHeaderGatewayFilterFactory;

    @Autowired
    SetResponseHeaderGatewayFilterFactory setResponseHeaderGatewayFilterFactory;

    @Autowired
    SetStatusGatewayFilterFactory setStatusGatewayFilterFactory;

    @Autowired
    StripPrefixGatewayFilterFactory stripPrefixGatewayFilterFactory;

    @Autowired
    RetryGatewayFilterFactory retryGatewayFilterFactory;

    @Autowired
    RequestSizeGatewayFilterFactory requestSizeGatewayFilterFactory;

    @Autowired
    SetRequestHostHeaderGatewayFilterFactory setRequestHostHeaderGatewayFilterFactory;


    protected void applyApikeyHeaderFilter(AtomicReference<BooleanSpec> b) {

        if (gatewayClass.hasFeature(GatewayClass.Feature.apikey)){
            b.get().filters(f -> f.filter(apikeyHeaderGateway.apply(config -> {
                // NO CFG
            })));
        }
    }

    /**
     * implements https://gateway-api.sigs.k8s.io/references/spec/#networking.x-k8s.io/v1alpha1.HTTPHeaderMatch
     * @param r
     * @param b
     */
    protected void applyHeaderFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, HTTPRequestHeaderFilterSpec hfs){

        //add
        if (hfs.getAdd().size() > 0){
            b.get().filters(f -> f.filter(addRequestHeaderGatewayFilterFactory.apply(config -> {
                // TODO implement
            })));
        }

        // set (map)
        if (hfs.getSet().size() > 0) {
            b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
                // TODO implement
            })));
        }

        // remove
        if (hfs.getRemove().size() > 0) {
            b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
                // TODO implement
            })));
        }
    }



    protected void applyAddRequestHeaderFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(addRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyAddRequestParameterFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyAddResponseHeaderFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyDedupeResponseHeaderFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyCircuitBreakerFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyFallbackHeadersFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyMapRequestHeaderFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyPrefixPathFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyPreserveHostFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyRequestRateLimiterFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyRedirectToFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyRemoveRequestHeaderFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyRemoveResponseHeaderFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyRemoveRequestParameterFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyRewritePathFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyRewriteLocationResponseHeaderFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyRewriteResponseHeaderFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applySaveSessionFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applySecureHeadersFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applySetPathFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applySetRequestHeaderFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applySetResponseHeaderFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applySetStatusFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyStripPrefixFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applyRetryFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(retryGatewayFilterFactory.apply(config -> {
            config.setRetries((Integer) configMap.getOrDefault("retries", 7));
            // TODO implement
        })));
    }
    protected void applyRequestSizeFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(removeRequestHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }
    protected void applySetRequestHostFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, Map<String, Object> configMap){
        b.get().filters(f -> f.filter(setRequestHostHeaderGatewayFilterFactory.apply(config -> {
            // TODO implement
        })));
    }






    public void applyCustomFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, LocalObjectReference extensionRef) {
        Map<String, String> extensionConfig = locateExtensionAsConfigMap(extensionRef);
        String filterName = extensionConfig.getOrDefault("name", "");
        Map<String, Object> args = parseArgs(extensionConfig.getOrDefault("args", ""));


        SCGFilters filter = SCGFilters.valueOf(filterName);
        
        switch (filter){
            case AddRequestHeader:
                break;
            case AddRequestParameter:
                break;
            case AddResponseHeader:
                break;
            case DedupeResponseHeader:
                break;
            case CircuitBreaker:
                break;
            case FallbackHeaders:
                break;
            case MapRequestHeader:
                break;
            case PrefixPath:
                break;
            case PreserveHost:
                break;
            case RequestRateLimiter:
                break;
            case RedirectTo:
                break;
            case RemoveRequestHeader:
                break;
            case RemoveResponseHeader:
                break;
            case RemoveRequestParameter:
                break;
            case RewritePath:
                break;
            case RewriteLocationResponseHeader:
                break;
            case RewriteResponseHeader:
                break;
            case SaveSession:
                break;
            case SecureHeaders:
                break;
            case SetPath:
                break;
            case SetRequestHeader:
                break;
            case SetResponseHeader:
                break;
            case SetStatus:
                break;
            case StripPrefix:
                break;
            case Retry:
                applyRetryFilter(r, b, args);
                break;
            case RequestSize:
                break;
            case SetRequestHost:
                break;

        }
    }

    private Map<String, String> locateExtensionAsConfigMap(LocalObjectReference extensionRef){

        if (extensionRef.getKind().equals("ConfigMap")
                && extensionRef.getGroup().equals("v1")){

                ConfigMap configMap = kubernetesClient
                        .configMaps()
                        .withName(extensionRef.getName()).get();
                if (ObjectUtils.allNotNull(configMap))
                    return configMap.getData();
        }

        return Collections.emptyMap();

    }

    private Map<String, Object> parseArgs(String args){
        Yaml yaml = new Yaml();
        return yaml.load(args);
    }

    private Map<String, Object> locateExtensionAsMap(LocalObjectReference extensionRef){

        CustomResourceDefinitionContext customResourceDefinitionContext = new CustomResourceDefinitionContext.Builder()
                .withKind(extensionRef.getKind())
                .withGroup(extensionRef.getGroup())
                .withName(extensionRef.getName())
                .build();

        return kubernetesClient.customResource(customResourceDefinitionContext).list();
    }
}
