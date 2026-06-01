package model;

public class FerrisWheel extends Attraction {
    public FerrisWheel(String name, int minAge, double minHeight) {
        super(name, AttractionType.FAMILY, minAge, minHeight);
    }

    @Override
    public String getDescription() {
        return "Сімейне колесо огляду з панорамним виглядом на весь парк.";
    }
}