package client.services;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Event;

import javax.inject.Inject;
import java.util.List;

public class EventInfoService {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;


    public MainCtrl getMainCtrl() {
        return mainCtrl;
    }

    /**
     * Constructor
     *
     * @param server
     * @param mainCtrl
     */
    @Inject
    public EventInfoService(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * @return get server instance
     */
    public ServerUtils getServer(){
        return this.server;
    }

    /**
     * @param selectedEvent event whose info we want
     */
    public void showEventInfo(Event selectedEvent) {
        mainCtrl.showEventInfo(selectedEvent);
    }

    /**
     *
     */
    public void showAdd() {
        mainCtrl.showAdd();
    }

    /**
     *
     */
    public void login() {
        mainCtrl.login();
    }

    /**
     * @return all server events
     */
    public List<Event> getEvents(){
        return server.getEvents();
    }

    /**
     * @param id id of event
     * @return  event
     */
    public Event getEventById(long id){
        return server.getEventById(id);
    }

    /**
     * @param event update event method
     */
    public void updateEvent(Event event){
        server.updateEvent(event);
    }

    /**
     * starts a new websocket session
     */
    public void setSession(){
        server.setSession();
    }
}
