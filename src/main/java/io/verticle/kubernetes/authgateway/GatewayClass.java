package io.verticle.kubernetes.authgateway;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GatewayClass {

    private static final Logger log = LoggerFactory.getLogger(GatewayClass.class);

    @Value("${"+ENV_GATEWAY_NAME+":unknown}")
    String gatewayNameFromEnv;

    public static final String LABEL_IO_VERTICLE_GATEWAY_NAME = "io.verticle.gateway.name";
    public static final String ENV_GATEWAY_NAME = "GATEWAY_NAME";

    @Autowired
    KubernetesClient kubernetesClient;

    public enum Feature {
        apikey, oidc
    }

    Map<Feature, Boolean> enabledFeatures = new HashMap<>();

    @PostConstruct
    private void init(){

        Map<String, String> configMapData = getConfigMapData();

        if (configMapData.size() > 0){
            enabledFeatures.put(Feature.apikey, isEnabled(configMapData,"apikeyEnabled"));
            enabledFeatures.put(Feature.oidc, isEnabled(configMapData,"oidcEnabled"));
        }

        log.info("enabled features: " + enabledFeatures.size());
        enabledFeatures.forEach((feature, aBoolean) -> {
            log.info("feature " + feature.name() + ": " + aBoolean);
        });
    }

    private boolean isEnabled(Map<String, String> config, String feature){
        return config.containsKey(feature) && Boolean.valueOf(config.get(feature));
    }

    @Cacheable
    public Map<String, String> getConfigMapData(){
        List<ConfigMap> configMaps = kubernetesClient
                .configMaps()
                .withLabel(LABEL_IO_VERTICLE_GATEWAY_NAME, gatewayNameFromEnv)
                .list().getItems();
        if (configMaps.size() == 0)
            return new HashMap<>();
        else
            return configMaps.get(0).getData();
    }

    public boolean hasFeature(Feature feature){
        return enabledFeatures.getOrDefault(feature, false);
    }
}
