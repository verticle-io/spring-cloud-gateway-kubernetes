package io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute;

public class HTTPRouteMatchSpec {

    HTTPPathMatchSpec path;
    //HTTPHeaderMatch headers;
    //HTTPQueryParamMatch queryParams;
    //LocalObjectReference extensionRef


    public HTTPPathMatchSpec getPath() {
        return path;
    }

    public void setPath(HTTPPathMatchSpec path) {
        this.path = path;
    }
}
