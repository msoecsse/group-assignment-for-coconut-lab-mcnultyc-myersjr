package Beach;

import coconuts.HitEvent;
import coconuts.HitEventType;
import coconuts.HittableIslandObject;

/**
 * Domain object representing the beach.
 */
public class Beach extends HittableIslandObject {

    public Beach(OhCoconutsGameManager game, int skyHeight, int islandWidth) {
        super(game, 0, skyHeight, islandWidth, null);
        // System.out.println("Beach at y = " + this.y);
    }

    @Override
    public void step() { /* do nothing */ }

    @Override
    public void onHit(HitEvent event) {
        if (event.getType() == HitEventType.GROUND_HIT && event.getHittingObject() != null) {
            containingGame.coconutDestroyed();
            containingGame.scheduleForDeletion(event.getHittingObject());
        }
    }
}