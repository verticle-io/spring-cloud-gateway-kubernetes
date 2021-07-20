package io.verticle.kubernetes.authgateway;


import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.*;
import io.verticle.apex.gateway.crd.v1alpha1.httproute.HTTPRoute;
import io.verticle.kubernetes.authgateway.route.RouteConfigService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class HttpRouteController implements ResourceController<HTTPRoute> {

    private static final Log log = LogFactory.getLog(HttpRouteController.class);
    private final KubernetesClient kubernetesClient;
    @Autowired
    RouteConfigService routeConfigService;

    public HttpRouteController(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public DeleteControl deleteResource(HTTPRoute resource, Context<HTTPRoute> context) {
        log.info("HttpRoute deleteResource for: " + resource.getMetadata().getName());

        return DeleteControl.DEFAULT_DELETE;
    }

    @Override
    public UpdateControl<HTTPRoute> createOrUpdateResource(
            HTTPRoute resource, Context<HTTPRoute> context) {
        log.info("HttpRoute createOrUpdateResource for: " + resource.getMetadata().getName());

        routeConfigService.mapCustomResource(resource);

        return UpdateControl.updateCustomResource(resource);
    }


}
