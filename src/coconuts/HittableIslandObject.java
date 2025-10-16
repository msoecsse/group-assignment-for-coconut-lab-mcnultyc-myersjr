package coconuts;

import Beach.OhCoconutsGameManager;
import javafx.scene.image.Image;

// Represents island objects which can be hit
// This is a domain class; do not introduce JavaFX or other GUI components here

//THIS IS THE OBSERVER
public abstract class HittableIslandObject extends IslandObject implements HitObserver {
    public HittableIslandObject(OhCoconutsGameManager game, int x, int y, int width, Image image) {
        super(game, x, y, width, image);
    }

    public boolean isHittable() {
        return true;
    }

    /**
     * Default behavior for an object when it is hit.
     * Subclasses should override this to define specific reactions to different hit types.
     */
    public abstract void onHit(HitEvent event);
}