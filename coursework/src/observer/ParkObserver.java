package observer;

public interface ParkObserver {
    void onEvent(String eventType, String message);
}