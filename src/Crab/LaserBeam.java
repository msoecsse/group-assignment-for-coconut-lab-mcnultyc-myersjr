package Crab;

import Beach.OhCoconutsGameManager;
import coconuts.IslandObject;
import javafx.scene.image.Image;

/**
 * Upward-moving laser beam emitted by the crab.
 */
public class LaserBeam extends IslandObject {
    private static final int WIDTH = 12; // must be updated with image
    private static final Image laserImage = new Image("file:images/laser-1.png");

    /**
     * Creates a laser beam at the crab's eye position.
     * @param game game context
     * @param eyeHeight initial y coordinate
     * @param crabCenterX initial x center of the laser
     */
    public LaserBeam(OhCoconutsGameManager game, int eyeHeight, int crabCenterX) {
        super(game, crabCenterX - WIDTH / 2, eyeHeight, WIDTH, laserImage);
    }

    public int hittable_height() {
        return y + WIDTH;
    }

    @Override
    public void step() {
        y -= 12;
        if (y + height < 0) {
            containingGame.scheduleForDeletion(this);
        }
    }

    @Override
    public boolean isFalling() {
        return false;
    }
}