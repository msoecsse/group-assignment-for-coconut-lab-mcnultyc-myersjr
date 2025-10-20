package coconuts;

import Beach.OhCoconutsGameManager;
import javafx.scene.image.Image;

/**
 * Base class for island objects that can be struck by other objects.
 */
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