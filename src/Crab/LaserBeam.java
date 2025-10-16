package Crab;

import Beach.OhCoconutsGameManager;
import coconuts.IslandObject;
import javafx.scene.image.Image;

// Represents the beam of light moving from the crab to a coconut; can hit only falling objects
// This is a domain class; do not introduce JavaFX or other GUI components here
public class LaserBeam extends IslandObject {
    private static final int WIDTH = 12; // must be updated with image
    private static final Image laserImage = new Image("file:images/laser-1.png");

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