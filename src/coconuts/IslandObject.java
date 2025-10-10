package coconuts;

import Beach.OhCoconutsGameManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// an object in the game, either something coming from the island or falling on it
// Each island object has a location and can determine if it hits another island object
// This is a domain class; other than Image and ImageView, do not introduce other JavaFX components here
public abstract class IslandObject {
    protected final int width;
    protected final OhCoconutsGameManager containingGame;
    protected int x, y;
    protected int height; // assuming a square for now
    ImageView imageView = null;

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

    public void display() {
        if (imageView != null) {
            imageView.setLayoutX(x);
            imageView.setLayoutY(y);
        }
    }

    public boolean isHittable() {
        return false;
    }

    protected int hittable_height() {
        return y + height;
    }

    public boolean isGroundObject() {
        return y >= containingGame.getHeight();
    }

    public boolean isFalling() {
        return !isGroundObject();
    }

    public boolean canHit(IslandObject other) {
        return this.isFalling() != other.isFalling();
    }

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
