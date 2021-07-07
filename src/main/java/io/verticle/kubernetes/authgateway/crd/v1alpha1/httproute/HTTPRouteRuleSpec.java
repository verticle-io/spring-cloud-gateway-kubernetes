package io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute;

public class HTTPRouteRuleSpec {

    HTTPRouteMatchListSpec matches;
    HTTPRouteForwardToListSpec forwardTo;
    HTTPRouteFilterListSpec filters;


    public HTTPRouteMatchListSpec getMatches() {
        return matches;
    }

    public void setMatches(HTTPRouteMatchListSpec matches) {
        this.matches = matches;
    }

    public HTTPRouteForwardToListSpec getForwardTo() {
        return forwardTo;
    }

    public void setForwardTo(HTTPRouteForwardToListSpec forwardTo) {
        this.forwardTo = forwardTo;
    }

    public HTTPRouteFilterListSpec getFilters() {
        return filters;
    }

    public void setFilters(HTTPRouteFilterListSpec filters) {
        this.filters = filters;
    }
}
