package Beach;

import Crab.Crab;
import Crab.LaserBeam;
import coconuts.Coconut;
import coconuts.HitEvent;
import coconuts.HitObserver;
import coconuts.HittableIslandObject;
import coconuts.IslandObject;
import coconuts.HitEventType;
import javafx.scene.layout.Pane;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Central game manager for Oh Coconuts
 */
public class OhCoconutsGameManager {
    private final Collection<IslandObject> allObjects = new LinkedList<>();
    private final Collection<HittableIslandObject> hittableIslandSubjects = new LinkedList<>();
    private final Collection<IslandObject> scheduledForRemoval = new LinkedList<>();
    private final Collection<HitObserver> globalHitObservers = new LinkedList<>();
    private final int height, width;
    private final int DROP_INTERVAL = 10;
    private final int MAX_TIME = 100;
    private Pane gamePane;
    private Crab theCrab;
    private Beach theBeach;
    private int coconutsInFlight = 0;
    private int gameTick = 0;

    /**
     * Constructs the game manager and initializes the beach and crab.
     * @param height vertical play area
     * @param width horizontal play area
     * @param gamePane JavaFX pane used to display IslandObject ImageViews
     */
    public OhCoconutsGameManager(int height, int width, Pane gamePane) {
        this.height = height;
        this.width = width;
        this.gamePane = gamePane;

        this.theCrab = new Crab(this, height, width);
        registerObject(theCrab);
        gamePane.getChildren().add(theCrab.getImageView());

        this.theBeach = new Beach(this, height, width);
        registerObject(theBeach);
        if (theBeach.getImageView() != null)
            System.out.println("Unexpected image view for beach");
    }

    private void registerObject(IslandObject object) {
        allObjects.add(object);
        if (object.isHittable()) {
            HittableIslandObject asHittable = (HittableIslandObject) object;
            hittableIslandSubjects.add(asHittable);
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    /**
     * Marks that a coconut was removed by some cause.
     */
    public void coconutDestroyed() {
        coconutsInFlight -= 1;
    }

    /**
     * Attempts to spawn a new Coconut at a periodic interval while the crab is alive.
     */
    public void tryDropCoconut() {
        if (gameTick % DROP_INTERVAL == 0 && theCrab != null) {
            coconutsInFlight += 1;
            Coconut c = new Coconut(this, (int) (Math.random() * width));
            registerObject(c);
            gamePane.getChildren().add(c.getImageView());
        }
        gameTick++;
    }

    public Crab getCrab() {
        return theCrab;
    }

    /**
     * Removes the crab from the game.
     */
    public void killCrab() {
        theCrab = null;
    }

    /**
     * Registers a global observer that will be attached to each HitEvent
     * before it is dispatched
     * @param observer observer to attach
     */
    public void addHitObserver(HitObserver observer) {
        globalHitObservers.add(observer);
    }

    /**
     * Unregisters a previously added global observer.
     * @param observer observer to remove
     */
    public void removeHitObserver(HitObserver observer) {
        globalHitObservers.remove(observer);
    }

    /**
     * Fires a laser from the crab's eye position and
     * adds it to the scene so it will be animated on subsequent ticks
     */
    public void fireLaser() {
        if (theCrab == null || theCrab.getImageView() == null) {
            return;
        }

        int eyeY = theCrab.eyeY();
        int centerX = theCrab.centerX();
        LaserBeam beam = new LaserBeam(this, eyeY, centerX);
        registerObject(beam);
        if (beam.getImageView() != null) {
            gamePane.getChildren().add(beam.getImageView());
        }
    }

    /**
     * Advances the simulation by one tick.
     */
    public void advanceOneTick() {
        for (IslandObject o : allObjects) {
            o.step();
            o.display();
        }

        scheduledForRemoval.clear();

        for (IslandObject thisObj : allObjects) {
            for (HittableIslandObject hittableObject : hittableIslandSubjects) {
                if (thisObj instanceof Coconut && hittableObject instanceof Crab
                        && thisObj.isTouching(hittableObject)) {
                    HitEvent event = new HitEvent(HitEventType.CRAB_HIT, hittableObject, thisObj);
                    for (HitObserver o : globalHitObservers) {
                        event.attach(o);
                    }
                    event.notifyObservers();
                }
            }
        }

        for (IslandObject thisObj : allObjects) {
            for (HittableIslandObject hittableObject : hittableIslandSubjects) {
                if (thisObj.canHit(hittableObject) && thisObj.isTouching(hittableObject)) {
                    if (scheduledForRemoval.contains(thisObj) || scheduledForRemoval.contains(hittableObject)) {
                        continue;
                    }
                    HitEvent event = getHitEvent(thisObj, hittableObject);
                    if (event.getType() != HitEventType.CRAB_HIT) {
                        for (HitObserver o : globalHitObservers) {
                            event.attach(o);
                        }
                        event.notifyObservers();
                    }
                }
            }
        }

        for (IslandObject o : allObjects) {
            if (o instanceof Coconut) {
                Coconut c = (Coconut) o;
                if (c.y + c.height > getHeight() && !scheduledForRemoval.contains(c)) {
                    HitEvent forced = new HitEvent(HitEventType.GROUND_HIT, theBeach, c);
                    for (HitObserver obs : globalHitObservers) {
                        forced.attach(obs);
                    }
                    forced.notifyObservers();
                }
            }
        }

        // actually remove the objects as needed
        for (IslandObject thisObj : scheduledForRemoval) {
            allObjects.remove(thisObj);
            if (thisObj instanceof HittableIslandObject) {
                hittableIslandSubjects.remove((HittableIslandObject) thisObj);
                gamePane.getChildren().remove(thisObj.getImageView());
            }
            if (thisObj.getImageView() != null) {
                gamePane.getChildren().remove(thisObj.getImageView());
            }
        }
        scheduledForRemoval.clear();
    }

    private HitEvent getHitEvent(IslandObject thisObj, HittableIslandObject hittableObject) {
        HitEventType type;
        if (thisObj instanceof LaserBeam && hittableObject instanceof Coconut) {
            type = HitEventType.LASER_HIT;
            coconutDestroyed();
        } else if ((thisObj instanceof Coconut && hittableObject instanceof Crab)
                || (thisObj instanceof Crab && hittableObject instanceof Coconut)) {
            type = HitEventType.CRAB_HIT;
        } else {
            type = HitEventType.GROUND_HIT;
        }
        return new HitEvent(type, hittableObject, thisObj);
    }

    /**
     * Schedules an IslandObject to be removed at the end of the current tick.
     * @param islandObject object to remove
     */
    public void scheduleForDeletion(IslandObject islandObject) {
        scheduledForRemoval.add(islandObject);
    }

    /**
     * true when no coconuts remain and the time horizon has elapsed,
     * or the crab has been destroyed.
     */
    public boolean done() {
        return (coconutsInFlight == 0 && gameTick >= MAX_TIME) || theCrab == null;
    }
}
