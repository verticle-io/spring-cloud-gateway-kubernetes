package io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

public class HTTPRouteFilterSpec {

    HTTPRouteFilterTypeEnum type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    HTTPRequestHeaderFilterSpec requestHeaderModifier;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    HTTPRequestMirrorFilterSpec requestMirror;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalObjectReference extensionRef;

    public HTTPRouteFilterTypeEnum getType() {
        return type;
    }

    public void setType(HTTPRouteFilterTypeEnum type) {
        this.type = type;
    }

    public HTTPRequestHeaderFilterSpec getRequestHeaderModifier() {
        return requestHeaderModifier;
    }

    public void setRequestHeaderModifier(HTTPRequestHeaderFilterSpec requestHeaderModifier) {
        this.requestHeaderModifier = requestHeaderModifier;
    }

    public HTTPRequestMirrorFilterSpec getRequestMirror() {
        return requestMirror;
    }

    public void setRequestMirror(HTTPRequestMirrorFilterSpec requestMirror) {
        this.requestMirror = requestMirror;
    }

    public LocalObjectReference getExtensionRef() {
        return extensionRef;
    }

    public void setExtensionRef(LocalObjectReference extensionRef) {
        this.extensionRef = extensionRef;
    }
}
