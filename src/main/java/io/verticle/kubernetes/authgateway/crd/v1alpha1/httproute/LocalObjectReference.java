package io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute;

public class LocalObjectReference {

    String kind;
    String group;
    String name;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
