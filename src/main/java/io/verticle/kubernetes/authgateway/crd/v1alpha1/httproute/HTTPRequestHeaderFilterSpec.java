package io.verticle.kubernetes.authgateway.crd.v1alpha1.httproute;

import java.util.Map;

public class HTTPRequestHeaderFilterSpec {
    Map<String, String> set;
    Map<String, String> add;
    Map<String, String> remove;

    public Map<String, String> getSet() {
        return set;
    }

    public void setSet(Map<String, String> set) {
        this.set = set;
    }

    public Map<String, String> getAdd() {
        return add;
    }

    public void setAdd(Map<String, String> add) {
        this.add = add;
    }

    public Map<String, String> getRemove() {
        return remove;
    }

    public void setRemove(Map<String, String> remove) {
        this.remove = remove;
    }
}
