package io.verticle.kubernetes.authgateway.apikeys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KeyService {

    @Autowired
    KubernetesSecretReader secretReader;

    public boolean match(String keyFromRequest, String namespace) {

        Map<String, Apikey> apikeyMap = secretReader.readApikeySecretsInNamespace(namespace);
        return apikeyMap.containsKey(keyFromRequest);
    }

}
