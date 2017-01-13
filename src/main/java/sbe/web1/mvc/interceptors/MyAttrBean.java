package sbe.web1.mvc.interceptors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAttrBean {
    static final Logger LOG = LoggerFactory.getLogger(MyAttrBean.class);

    List<String> uuids = new ArrayList<>();

    public MyAttrBean() {
        uuids.add(UUID.randomUUID().toString());
        LOG.info("########R# MyAttrBean constructor() : {}", String.join(",", this.uuids));
    }

    public MyAttrBean(String first) {
        uuids.add(first);
        uuids.add(UUID.randomUUID().toString());
        LOG.info("########R# MyAttrBean constructor({}) : {}", first, String.join(",", this.uuids));
    }

    public void addUUID() {
        uuids.add(UUID.randomUUID().toString());
    }

    public void addString(String s) {
        uuids.add(s);
    }

    public List<String> getUuids() {
        return new ArrayList<>(uuids);
    }

    @Override
    public String toString() {
        return "MyAttrBean [uuids=" + String.join(",", this.uuids) + "]";
    }
}
