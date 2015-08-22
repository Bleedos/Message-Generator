package com.shebdev.sclermont.myfirstapp.db;

/**
 * Created by sclermont on 22/08/15.
 */
public class MessageAssemblyLinkData {
    
    private long id;
    private String linkId;
    private String assemblyId;
    private String partId;
    private String partOrder;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getAssemblyId() {
        return assemblyId;
    }

    public void setAssemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getPartOrder() {
        return partOrder;
    }

    public void setPartOrder(String partOrder) {
        this.partOrder = partOrder;
    }
}
