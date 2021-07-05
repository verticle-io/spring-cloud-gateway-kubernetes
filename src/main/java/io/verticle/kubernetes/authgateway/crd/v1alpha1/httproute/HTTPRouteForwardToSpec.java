package io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute;

public class HTTPRouteForwardToSpec {

    int port;
    String serviceName;
    int weight;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
