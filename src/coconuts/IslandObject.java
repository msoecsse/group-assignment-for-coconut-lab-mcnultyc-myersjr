package coconuts;

import Beach.OhCoconutsGameManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Base class for all island domain objects.
 */
public abstract class IslandObject {
    protected final int width;
    protected final OhCoconutsGameManager containingGame;
    public int x, y;
    public int height; // assuming a square for now
    ImageView imageView = null;

    /**
     * Constructs an island object at a location with an optional image.
     * @param game game context
     * @param x left coordinate
     * @param y top coordinate
     * @param width display width
     * @param image image to display
     */
    public IslandObject(OhCoconutsGameManager game, int x, int y, int width, Image image) {
        containingGame = game;
        if (image != null) {
            imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(width);
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = width; // assume square
        display();
        //System.out.println(this + " left " + left() + " right " + right());
    }

    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Updates the JavaFX node position to match the object's current x/y.
     */
    public void display() {
        if (imageView != null) {
            imageView.setLayoutX(x);
            imageView.setLayoutY(y);
        }
    }

    public boolean isHittable() {
        return false;
    }

    /**
     * @return the y coordinate used when this object is considered hit
     */
    protected int hittable_height() {
        return y + height;
    }

    /**
     * @return true if the object is on/at the ground line
     */
    public boolean isGroundObject() {
        return y >= containingGame.getHeight();
    }

    /**
     * @return true if the object is above ground and therefore falling
     */
    public boolean isFalling() {
        return !isGroundObject();
    }

    /**
     * Determines if this object can hit another based on motion type.
     */
    public boolean canHit(IslandObject other) {
        return this.isFalling() != other.isFalling();
    }

    /**
     * Axis-aligned bounding-box overlap test
     * @param other the other object
     * @return true if rectangles overlap
     */
    public boolean isTouching(IslandObject other) {
        // this object's bounds
        int thisLeft = this.x;
        int thisRight = this.x + this.width;
        int thisTop = this.y;
        int thisBottom = this.y + this.height;

        // other object's bounds
        int otherLeft = other.x;
        int otherRight = other.x + other.width;
        int otherTop = other.y;
        int otherBottom = other.y + other.height;

        return thisLeft < otherRight && thisRight > otherLeft &&
               thisTop < otherBottom && thisBottom > otherTop;
    }

    public abstract void step();
}
