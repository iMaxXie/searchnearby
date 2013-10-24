package com.beyond.SearchNearBy.model;

import com.baidu.location.BDLocation;

public class Location {
    private BDLocation location = null;

    public BDLocation getLocation() {
        return location;
    }

    public void setLocation(BDLocation location) {
        this.location = location;
    }
}
