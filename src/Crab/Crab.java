package Crab;

import coconuts.HitEvent;
import coconuts.HitEventType;
import coconuts.HittableIslandObject;
import Beach.OhCoconutsGameManager;
import javafx.scene.image.Image;

import java.util.Objects;

// Represents the object that shoots down coconuts but can be hit by coconuts. Killing the
//   crab ends the game
// This is a domain class; other than Image, do not introduce JavaFX or other GUI components here
public class Crab extends HittableIslandObject {
    private static final int WIDTH = 50; // assumption: height and width are the same
    private static final Image crabImage = new Image("file:images/crab-1.png");
    private static final int DEFAULT_MAX_HP = 3;
    private int maxHP = DEFAULT_MAX_HP;
    private int hp = DEFAULT_MAX_HP;

    public Crab(OhCoconutsGameManager game, int skyHeight, int islandWidth) {
        super(game, islandWidth / 2, skyHeight, WIDTH, crabImage);
    }

    @Override
    public void step() {
        // do nothing
    }

    // Captures the crab crawling sideways
    public void crawl(int offset) {
        x += offset;
        int maxX = containingGame.getWidth() - this.width;
        if (x < 0) {
            x = 0;
        }
        if (x > maxX) {
            x = maxX;
        }
        display();
    }

    @Override
    public void onHit(HitEvent event) {
        if (Objects.requireNonNull(event.getType()) == HitEventType.CRAB_HIT) {
            hp -= 1;
            if (hp <= 0) {
                containingGame.scheduleForDeletion(this);
                containingGame.killCrab();
            }
        }
    }

    public int eyeY() {
        return y;
    }
    public int centerX() {
        return x + (width / 2);
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHP;
    }
}
