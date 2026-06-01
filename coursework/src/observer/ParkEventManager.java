package observer;

import java.util.ArrayList;
import java.util.List;

public class ParkEventManager {
    private final List<ParkObserver> listeners = new ArrayList<>();

    public void subscribe(ParkObserver listener) {
        listeners.add(listener);
    }

    public void notify(String eventType, String message) {
        for (ParkObserver listener : listeners) {
            listener.onEvent(eventType, message);
        }
    }
}