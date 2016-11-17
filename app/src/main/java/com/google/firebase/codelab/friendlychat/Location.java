package com.google.firebase.codelab.friendlychat;

/**
 * Created by john on 11/7/16.
 */
public class Location {
    public String sitename;
    public String deviceName;
    public String scantype;
    public Long siteid;
    public String name;
    public String locationid;
    public String site_id;
    public String deviceType;
    public String deviceid;
    public String direction;

    public Location() {
    }

    public Location(String sitename, String deviceName, String scantype, Long siteid, String name,
                    String locationid, String site_id, String deviceType, String deviceid, String direction) {
        this.sitename = sitename;
        this.deviceName = deviceName;
        this.scantype = scantype;
        this.siteid = siteid;
        this.name = name;
        this.locationid = locationid;
        this.site_id = site_id;
        this.deviceType = deviceType;
        this.deviceid = deviceid;
        this.direction = direction;
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getScantype() {
        return scantype;
    }

    public Long getSiteid() {
        return siteid;
    }

    public String getName() {
        return name;
    }

    public String getLocationid() {
        return locationid;
    }

    public String getSite_id() {
        return site_id;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDirection() {
        return direction;
    }
}
