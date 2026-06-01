package observer;

import java.util.HashMap;
import java.util.Map;

public class StatisticsCollector implements ParkObserver {
    private final Map<String, Integer> eventStats = new HashMap<>();

    @Override
    public void onEvent(String eventType, String message) {
        eventStats.put(eventType, eventStats.getOrDefault(eventType, 0) + 1);
        System.out.println("[ЛОГ] [" + eventType + "]: " + message);
    }

    public void printStatistics() {
        System.out.println("\n=== СТАТИСТИКА СИСТЕМИ ===");
        if (eventStats.isEmpty()) {
            System.out.println("Немає накопичених даних про події.");
        } else {
            eventStats.forEach((type, count) -> System.out.println("Подія '" + type + "': викликана " + count + " раз(ів)."));
        }
        System.out.println("==========================");
    }
}