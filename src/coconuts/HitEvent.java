package coconuts;

import java.util.ArrayList;
import java.util.List;

// An abstraction of all objects that can be hit by another object
// This captures the Subject side of the Observer pattern; observers of the hit event will take action
//   to process that event
// This is a domain class; do not introduce JavaFX or other GUI components here
public class HitEvent {
    private final List<HitObserver> observers = new ArrayList<>();
    private final HitEventType type;
    private final HittableIslandObject hitObject;
    private final IslandObject hittingObject;

    public HitEvent(HitEventType type, HittableIslandObject hitObject, IslandObject hittingObject) {
        this.type = type;
        this.hitObject = hitObject;
        this.hittingObject = hittingObject;
        attach(hitObject);
    }

    public void attach(HitObserver observer) {
        observers.add(observer);
    }

    public void detach(HitObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (HitObserver observer : observers) {
            observer.onHit(this);
        }
    }

    public HitEventType getType() {
        return type;
    }
}
