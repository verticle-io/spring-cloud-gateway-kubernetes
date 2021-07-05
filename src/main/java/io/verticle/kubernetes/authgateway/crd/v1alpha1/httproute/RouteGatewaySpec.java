package io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute;

public class RouteGatewaySpec {

    GatewayAllowTypeSpec allow;
    GatewayReferenceListSpec gatewayRefs;
    public RouteGatewaySpec() {
    }

    public GatewayAllowTypeSpec getAllow() {
        return allow;
    }

    public void setAllow(GatewayAllowTypeSpec allow) {
        this.allow = allow;
    }

    public GatewayReferenceListSpec getGatewayRefs() {
        return gatewayRefs;
    }

    public void setGatewayRefs(GatewayReferenceListSpec gatewayRefs) {
        this.gatewayRefs = gatewayRefs;
    }

    /*
    public List<GatewayReferenceSpec> getGatewayRefs() {
        return gatewayRefs;
    }

    public void setGatewayRefs(List<GatewayReferenceSpec> gatewayRefs) {
        this.gatewayRefs = gatewayRefs;
    }

     */
}
