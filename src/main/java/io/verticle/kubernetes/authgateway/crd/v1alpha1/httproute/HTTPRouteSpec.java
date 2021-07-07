package io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute;


public class HTTPRouteSpec {

    RouteGatewaySpec gateways;

    HostnameSpec hostnames;

    HTTPRouteRuleListSpec rules;

    public RouteGatewaySpec getGateways() {
        return gateways;
    }

    public void setGateways(RouteGatewaySpec gateways) {
        this.gateways = gateways;
    }

    public HostnameSpec getHostnames() {
        return hostnames;
    }

    public void setHostnames(HostnameSpec hostnames) {
        this.hostnames = hostnames;
    }

    public HTTPRouteRuleListSpec getRules() {
        return rules;
    }

    public void setRules(HTTPRouteRuleListSpec rules) {
        this.rules = rules;
    }
}