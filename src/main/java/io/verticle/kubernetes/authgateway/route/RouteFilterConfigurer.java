package io.verticle.kubernetes.authgateway.route;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.verticle.kubernetes.authgateway.GatewayClass;
import io.verticle.apex.gateway.crd.v1alpha1.httproute.HTTPRequestHeaderFilterSpec;
import io.verticle.apex.gateway.crd.v1alpha1.httproute.LocalObjectReference;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class RouteFilterConfigurer {

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    GatewayClass gatewayClass;

    static final String FEATURE_APIKEY_ENABELED = "apikeyEnabled";

    @Autowired
    KubernetesClient kubernetesClient;

    // verticle filters


    @Autowired
    List<GatewayFilterFactory> gatewayFilterList = new ArrayList<>();
    Map<String, GatewayFilterFactory> gatewayFilters = new HashMap<>();


    @PostConstruct
    public void initialize(){
        gatewayFilterList.forEach(filter -> {
            gatewayFilters.put(filter.name(), filter);
        });
    }

    protected void applyApikeyHeaderFilter(AtomicReference<BooleanSpec> b) {

        GatewayFilterFactory factory = gatewayFilters.get("ApikeyHeader");

        if (gatewayClass.hasFeature(GatewayClass.Feature.apikey)){
            b.get().filters(f -> f.filter(factory.apply(config -> {
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
        final GatewayFilterFactory addfactory = gatewayFilters.get("AddRequestHeader");
        if (hfs.getAdd().size() > 0){
            b.get().filters(f -> f.filter(addfactory.apply(
                    configurationService.with(addfactory)
                    .properties(hfs.getAdd())
                    .bind()
            )));
        }

        // set (map)
        final GatewayFilterFactory setFactory = gatewayFilters.get("SetRequestHeader");
        if (hfs.getSet().size() > 0) {
            b.get().filters(f -> f.filter(setFactory.apply(
                    configurationService.with(setFactory)
                            .properties(hfs.getAdd())
                            .bind()
            )));
        }

        // remove
        final GatewayFilterFactory removeFactory = gatewayFilters.get("RemoveRequestHeader");
        if (hfs.getRemove().size() > 0) {
            b.get().filters(f -> f.filter(removeFactory.apply(
                    configurationService.with(removeFactory)
                            .properties(hfs.getAdd())
                            .bind()
            )));
        }
    }


    protected void applyNamedFilter(AtomicReference<BooleanSpec> b, Map<String, String> configMap, String filterFactoryName){

        GatewayFilterFactory factory = gatewayFilters.get(filterFactoryName);

        b.get().filters(f -> f.filter(factory.apply(
                configurationService.with(factory)
                        .name(filterFactoryName)
                        .properties(configMap)
                        .bind()
        )));
    }



    public void applyCustomFilter(PredicateSpec r, AtomicReference<BooleanSpec> b, LocalObjectReference extensionRef) {
        Map<String, String> extensionConfig = locateExtensionAsConfigMap(extensionRef);
        String filterName = extensionConfig.getOrDefault("name", "");
        Map<String, String> args = parseArgs(extensionConfig.getOrDefault("args", ""));

        GatewayFilterFactory factory = gatewayFilters.get(filterName);

        b.get().filters(f -> f.filter(factory.apply(
                configurationService.with(factory)
                        .name(filterName)
                        .properties(args)
                        .bind()
        )));


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

    private Map<String, String> parseArgs(String args){
        Yaml yaml = new Yaml();

        Map<String, Object> map = yaml.load(args);
        Map<String,String> newMap =new HashMap<String,String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if(entry.getValue() instanceof String){
                newMap.put(entry.getKey(), (String) entry.getValue());
            } else {
                newMap.put(entry.getKey(),String.valueOf(entry.getValue()));
            }
        }

        return newMap;
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
