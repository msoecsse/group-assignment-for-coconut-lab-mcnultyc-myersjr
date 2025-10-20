package coconuts;

import Beach.OhCoconutsGameManager;
import javafx.scene.image.Image;

/**
 * A falling coconut that can hurt the crab or be destroyed by a laser.
 */
public class Coconut extends HittableIslandObject {
    private static final int WIDTH = 50;
    private static final Image coconutImage = new Image("file:images/coco-1.png");

    /**
     * Creates a coconut at the top of the screen at the given x coordinate
     * @param game game context
     * @param x initial horizontal position
     */
    public Coconut(OhCoconutsGameManager game, int x) {
        super(game, x, 0, WIDTH, coconutImage);
    }

    @Override
    public void step() {
        y += 5;
    }

    @Override
    public void onHit(HitEvent event) {
        if (event.getType() == HitEventType.LASER_HIT) {
            containingGame.scheduleForDeletion(this);
        } else if (event.getType() == HitEventType.CRAB_HIT) {
            containingGame.scheduleForDeletion(this);
        }
    }
}