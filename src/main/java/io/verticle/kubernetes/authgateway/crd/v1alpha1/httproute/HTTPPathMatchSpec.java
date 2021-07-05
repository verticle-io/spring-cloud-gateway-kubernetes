package io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute;

public class HTTPPathMatchSpec {

    PathMatchType type;
    String value;

    public PathMatchType getType() {
        return type;
    }

    public void setType(PathMatchType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    enum PathMatchType {
        Exact, Prefix
    }
}
