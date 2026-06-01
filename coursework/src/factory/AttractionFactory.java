package factory;

import model.*;

public class AttractionFactory {
    public static Attraction createAttraction(String name, AttractionType type) {
        return switch (type) {
            case EXTREME -> new RollerCoaster(name, 16, 140.0);
            case CHILDREN -> new Carousel(name, 5, 90.0);
            case FAMILY -> new FerrisWheel(name, 7, 110.0);
        };
    }
}