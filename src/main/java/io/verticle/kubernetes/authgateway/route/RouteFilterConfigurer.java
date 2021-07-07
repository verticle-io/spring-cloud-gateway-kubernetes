package io.verticle.kubernetes.authgateway.route;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.verticle.kubernetes.authgateway.ApikeyHeaderGatewayFilterFactory;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class RouteFilterConfigurer {


    static final String FEATURE_APIKEY_ENABELED = "apikeyEnabled";

    @Autowired
    KubernetesClient kubernetesClient;


    @Cacheable
    public Map<String, String> getConfigMapData(){
        List<ConfigMap> configs = kubernetesClient.configMaps().inNamespace("").withLabel("").list().getItems();
        if (configs.size() == 0)
            return new HashMap<>();
        else
            return configs.get(0).getData();
    }



    @Autowired
    ApikeyHeaderGatewayFilterFactory apikeyHeaderGateway;


    public void applyApikeyHeaderFilter(AtomicReference<BooleanSpec> b) {

        if (ObjectUtils.isNotEmpty(getConfigMapData().get(FEATURE_APIKEY_ENABELED))){
            b.get().filters(f -> f.filter(apikeyHeaderGateway.apply(config -> {
                // NO CFG
            })));
        }
    }
}
