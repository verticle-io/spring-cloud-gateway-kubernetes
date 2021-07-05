package io.verticle.kubernetes.authgateway.crd.v1alpha1.gateway;

public class GatewayStatus {

    ConditionsSpec[] conditions;

    public ConditionsSpec[] getConditions() {
        return conditions;
    }

    public void setConditions(ConditionsSpec[] conditions) {
        this.conditions = conditions;
    }
}
