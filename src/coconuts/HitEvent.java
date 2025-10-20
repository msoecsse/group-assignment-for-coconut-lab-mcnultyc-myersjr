package coconuts;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject representing a single collision (hit) event in the game.
 */
public class HitEvent {
    private final List<HitObserver> observers = new ArrayList<>();
    private final HitEventType type;
    private final HittableIslandObject hitObject;
    private final IslandObject hittingObject;

    /**
     * Constructs a hit event of a given type between two objects.
     * @param type the kind of hit
     * @param hitObject the object being struck
     * @param hittingObject the object causing the hit
     */
    public HitEvent(HitEventType type, HittableIslandObject hitObject, IslandObject hittingObject) {
        this.type = type;
        this.hitObject = hitObject;
        this.hittingObject = hittingObject;
        attach(hitObject);
        if (hittingObject instanceof HittableIslandObject) {
            attach((HittableIslandObject) hittingObject);
        }
    }

    /**
     * Attaches an observer to this event
     */
    public void attach(HitObserver observer) {
        observers.add(observer);
    }

    /**
     * Detaches a previously attached observer
     */
    public void detach(HitObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all attached observers of this hit
     */
    public void notifyObservers() {
        for (HitObserver observer : observers) {
            observer.onHit(this);
        }
    }

    public HitEventType getType() {
        return type;
    }

    public HittableIslandObject getHitObject() {
        return hitObject;
    }

    public IslandObject getHittingObject() {
        return hittingObject;
    }
}
