package memento;

import model.Attraction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParkMemento implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final List<Attraction> attractionsState;

    public ParkMemento(List<Attraction> attractions) {
        this.attractionsState = deepCopy(attractions);
    }

    public List<Attraction> getSavedState() {
        return deepCopy(this.attractionsState);
    }

    @SuppressWarnings("unchecked")
    private List<Attraction> deepCopy(List<Attraction> original) {
        try {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos);
            oos.writeObject(original);
            oos.flush();
            
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bos.toByteArray());
            java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bis);
            return (List<Attraction>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}