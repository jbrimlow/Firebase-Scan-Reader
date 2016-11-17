package com.google.firebase.codelab.friendlychat;

/**
 * Created by john on 11/16/16.
 */
public class Worker {
    String lastname;
    String firstname;
    String gateScanId;
    Long id;
    boolean active;
    String contractor;

    public Worker() {
    }

    public Worker(String lastname, String firstname, String gateScanId, Long id, boolean active, String contractor) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.gateScanId = gateScanId;
        this.id = id;
        this.active = active;
        this.contractor = contractor;
    }

    public boolean isActive() {
        return active;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getContractor() {
        return contractor;
    }

    public String getLastname() {
        return lastname;
    }

    public String getGateScanId() {
        return gateScanId;
    }

    public Long getId() {
        return id;
    }
}
