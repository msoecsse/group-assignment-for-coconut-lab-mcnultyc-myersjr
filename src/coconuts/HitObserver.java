package coconuts;

// The Observer in the Observer pattern.
// Observers are notified of a HitEvent.
public interface HitObserver {
    void onHit(HitEvent event);
}