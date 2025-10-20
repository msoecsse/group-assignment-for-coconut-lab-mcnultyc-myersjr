package coconuts;

/**
 * Observer in the Observer pattern for hit events
 */
public interface HitObserver {
    void onHit(HitEvent event);
}