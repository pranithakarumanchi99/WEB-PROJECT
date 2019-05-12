package app.psychic.models;

import java.io.Serializable;

public class Doctor implements Serializable {

    private String name;
    private String location;
    private String contact;

    public Doctor(String name, String location, String contact) {
        this.name = name;
        this.location = location;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
