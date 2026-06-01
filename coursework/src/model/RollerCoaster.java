package model;

public class RollerCoaster extends Attraction {
    public RollerCoaster(String name, int minAge, double minHeight) {
        super(name, AttractionType.EXTREME, minAge, minHeight);
    }

    @Override
    public String getDescription() {
        return "Екстремальні американські гірки з крутими віражами.";
    }
}