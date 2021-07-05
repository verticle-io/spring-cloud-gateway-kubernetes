package io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute;

public class HTTPRouteRuleSpec {

    HTTPRouteMatchListSpec matches;
    HTTPRouteForwardToListSpec forwardTo;

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
}
