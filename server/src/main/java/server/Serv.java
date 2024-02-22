package server;

import commons.Admin;
import commons.Event;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class Serv {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @OneToMany(cascade = CascadeType.PERSIST)
    public Admin admin;

    List<Admin> admins;
    List<Event> events;
    public Serv() {
        admins = new ArrayList<>();
        events = new ArrayList<>();
    }

    public Serv(Admin admin) {
        this.admins = new ArrayList<>();
        admins.add(admin);
        events = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Serv serv = (Serv) o;

        if (getId() != serv.getId()) return false;
        if (getAdmin() != null ? !getAdmin().equals(serv.getAdmin()) : serv.getAdmin() != null) return false;
        if (getAdmins() != null ? !getAdmins().equals(serv.getAdmins()) : serv.getAdmins() != null) return false;
        return getEvents() != null ? getEvents().equals(serv.getEvents()) : serv.getEvents() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getAdmin() != null ? getAdmin().hashCode() : 0);
        result = 31 * result + (getAdmins() != null ? getAdmins().hashCode() : 0);
        result = 31 * result + (getEvents() != null ? getEvents().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Serv{" +
                "id=" + id +
                ", admin=" + admin +
                ", admins=" + admins +
                ", events=" + events +
                '}';
    }
}
