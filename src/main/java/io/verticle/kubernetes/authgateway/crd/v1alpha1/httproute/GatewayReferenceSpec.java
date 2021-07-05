package io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute;

public class GatewayReferenceSpec {

    String name;
    String namespace;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
