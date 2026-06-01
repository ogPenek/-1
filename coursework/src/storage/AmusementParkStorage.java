package storage;

import memento.ParkMemento;
import model.Attraction;
import model.Pass;
import java.io.Serializable;
import observer.ParkEventManager;
import java.util.ArrayList;
import java.util.List;

public class AmusementParkStorage implements Serializable {
    private static final long serialVersionUID = 1L;

    private static AmusementParkStorage instance;
    
    private final List<Attraction> attractions;
    private final List<Pass> soldPasses;
    
    private transient ParkEventManager eventManager;

    private AmusementParkStorage() {
        this.attractions = new ArrayList<>();
        this.soldPasses = new ArrayList<>();
        this.eventManager = new ParkEventManager();
    }

    public static synchronized AmusementParkStorage getInstance() {
        if (instance == null) {
            instance = new AmusementParkStorage();
        }
        return instance;
    }

    public void addAttraction(Attraction attraction) {
        attractions.add(attraction);
        getEventManager().notify("ATTRACTION_ADDED", "Додано новий атракціон: " + attraction.getName());
    }

    public void registerPassSale(Pass pass) {
        soldPasses.add(pass);
        String typeStr = pass.isSubscription() ? "Абонемент" : "Квиток";
        getEventManager().notify("TICKET_SOLD", "Продано " + typeStr + " ID: " + pass.getId() + " на тип: " + pass.getAllowedType());
    }

    public List<Attraction> getAttractions() {
        return attractions;
    }

    public List<Pass> getSoldPasses() {
        return soldPasses;
    }

    public ParkEventManager getEventManager() {
        if (eventManager == null) {
            eventManager = new ParkEventManager();
        }
        return eventManager;
    }

    public ParkMemento save() {
        getEventManager().notify("SYSTEM_SAVE", "Створено точку збереження стану парку.");
        return new ParkMemento(this.attractions);
    }

    public void restore(ParkMemento memento) {
        if (memento != null) {
            this.attractions.clear();
            this.attractions.addAll(memento.getSavedState());
            getEventManager().notify("SYSTEM_RESTORE", "Стан парку успішно відновлено зі знімка.");
        }
    }
}