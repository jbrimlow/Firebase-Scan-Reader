package com.google.firebase.codelab.friendlychat;

/**
 * Created by john on 11/7/16.
 */
public class location {
    public String deviceid;
    public String deviceName;
    public String deviceType;
    public String direction;
    public String locationid;
    public String name;
    public String scantype;
    public String site_id;
    public Long siteid;
    public String sitename;

    public location(String deviceid, String deviceName, String deviceType, String direction, String locationid,
                    String name, String scantype, String site_id, Long siteid, String sitename) {
        this.deviceid = deviceid;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.direction = direction;
        this.locationid = locationid;
        this.name = name;
        this.scantype = scantype;
        this.site_id = site_id;
        this.siteid = siteid;
        this.sitename = sitename;
    }
}
