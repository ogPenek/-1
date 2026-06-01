package model;

import java.io.Serializable;

public class Pass implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final AttractionType allowedType;
    private final boolean isSubscription; // true - абонемент, false - одноразовий квиток
    private int usesLeft;

    public Pass(String id, AttractionType allowedType, boolean isSubscription) {
        this.id = id;
        this.allowedType = allowedType;
        this.isSubscription = isSubscription;
        this.usesLeft = isSubscription ? 10 : 1; // Абонемент на 10 поїздок, квиток на 1
    }

    public boolean use() {
        if (usesLeft > 0) {
            usesLeft--;
            return true;
        }
        return false;
    }

    public String getId() { return id; }
    public AttractionType getAllowedType() { return allowedType; }
    public boolean isSubscription() { return isSubscription; }
    public int getUsesLeft() { return usesLeft; }
}