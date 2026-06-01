package model;

import java.io.Serializable;

public abstract class Attraction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String name;
    private final AttractionType type;
    private final int minAge;
    private final double minHeight;
    private int visitorCount;
    private boolean underMaintenance;

    public Attraction(String name, AttractionType type, int minAge, double minHeight) {
        this.name = name;
        this.type = type;
        this.minAge = minAge;
        this.minHeight = minHeight;
        this.visitorCount = 0;
        this.underMaintenance = false;
    }

    public boolean canAccess(int age, double height) {
        return age >= minAge && height >= minHeight && !underMaintenance;
    }

    public void incrementVisitors() {
        this.visitorCount++;
    }

    public abstract String getDescription();

    public String getName() { return name; }
    public AttractionType getType() { return type; }
    public int getMinAge() { return minAge; }
    public double getMinHeight() { return minHeight; }
    public int getVisitorCount() { return visitorCount; }
    public boolean isUnderMaintenance() { return underMaintenance; }
    public void setUnderMaintenance(boolean underMaintenance) { this.underMaintenance = underMaintenance; }
}