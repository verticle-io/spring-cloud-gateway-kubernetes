package io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.ShortNames;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("networking.x-k8s.io")
@Version("v1alpha1")
@ShortNames("hr")
public class HTTPRoute extends CustomResource<HTTPRouteSpec, HTTPRouteStatus> implements Namespaced {
}