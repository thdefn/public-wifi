package com.zerobase.publicwifi;


import java.util.Date;

public class History {
    private Integer id;
    private Float xCoord;
    private Float yCoord;
    private Date createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getxCoord() {
        return xCoord;
    }

    public void setxCoord(Float xCoord) {
        this.xCoord = xCoord;
    }

    public Float getyCoord() {
        return yCoord;
    }

    public void setyCoord(Float yCoord) {
        this.yCoord = yCoord;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
