package model;

public class Carousel extends Attraction {
    public Carousel(String name, int minAge, double minHeight) {
        super(name, AttractionType.CHILDREN, minAge, minHeight);
    }

    @Override
    public String getDescription() {
        return "Дитяча карусель з дерев'яними конячками та музикою.";
    }
}