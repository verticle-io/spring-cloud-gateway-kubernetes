package io.verticle.kubernetes.authgateway.apikeys;

import io.fabric8.kubernetes.api.model.SecretList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class KubernetesSecretReader {

    Log log = LogFactory.getLog(this.getClass());

    public static final String KEY = "key";
    public static final String NAME = "name";
    public static final String OWNER = "owner";
    public static final String PURPOSE = "purpose";
    public static final String LABEL_VERTICLE_AUTHGATEWAY = "io.verticle.authgateway";
    public static final String LABEL_APIKEY = "apikey";

    @Cacheable("secrets")
    public Map<String, Apikey> readApikeySecretsInNamespace(){
        Map<String, Apikey> apikeyList = new HashMap();

        KubernetesClient client = new DefaultKubernetesClient();
        SecretList secretList = client.secrets().inNamespace("default").withLabel(LABEL_VERTICLE_AUTHGATEWAY, LABEL_APIKEY).list();
        secretList.getItems().forEach(s -> {
            log.info("loading secret " + s);
            log.info("secret data " + s.getData() );


            if (s != null){
                Map<String,String> data = s.getData();
                if (data != null){
                    Apikey key = new Apikey();

                    key.setKey(decode(data.get(KEY)));
                    key.setName(decode(data.get(NAME)));
                    key.setName(decode(data.get(OWNER)));
                    key.setName(decode(data.get(PURPOSE)));
                    apikeyList.put(key.getKey(), key);
                }
            }
        });

        return apikeyList;
    }

    private String decode(String base64){
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        String decodedString = new String(decodedBytes);
        return decodedString;
    }
}
