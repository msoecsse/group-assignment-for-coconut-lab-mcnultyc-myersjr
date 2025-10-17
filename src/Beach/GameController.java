package Beach;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.application.Platform;

import coconuts.HitEvent;
import coconuts.HitEventType;
import coconuts.HitObserver;


// JavaFX Controller class for the game - generally, JavaFX elements (other than Image) should be here
public class GameController {

    /**
     * Time between calls to step() (ms)
     */
    private static final double MILLISECONDS_PER_STEP = 1000.0 / 30;
    private Timeline coconutTimeline;
    private boolean started = false;

    @FXML
    private Pane gamePane;
    @FXML
    private Pane theBeach;
    @FXML
    private Label shotCoconutsLabel;
    @FXML
    private Label escapedCoconutsLabel;
    @FXML
    private Label destroyedCoconutsLabel;
    @FXML
    private Label healthLabel;

    private OhCoconutsGameManager theGame;

    private int shotsFired = 0;
    private int destroyedCount = 0;

    @FXML
    public void initialize() {
        theGame = new OhCoconutsGameManager((int) (gamePane.getPrefHeight() - theBeach.getPrefHeight()),
                (int) (gamePane.getPrefWidth()), gamePane);

        gamePane.setFocusTraversable(true);

        if (shotCoconutsLabel != null) shotCoconutsLabel.setText(Integer.toString(shotsFired));
        if (escapedCoconutsLabel != null) escapedCoconutsLabel.setText("0");
        if (destroyedCoconutsLabel != null) destroyedCoconutsLabel.setText(Integer.toString(destroyedCount));
        if (healthLabel != null) {
            healthLabel.setText(theGame.getCrab().getHp() + "/" + theGame.getCrab().getMaxHp());
        }

        theGame.addHitObserver(new HitObserver() {
            @Override
            public void onHit(HitEvent event) {
                if (event.getType() == HitEventType.LASER_HIT) {
                    destroyedCount++;
                    if (destroyedCoconutsLabel != null) {
                        Platform.runLater(() -> destroyedCoconutsLabel.setText(Integer.toString(destroyedCount)));
                    }
                } else if (event.getType() == HitEventType.CRAB_HIT) {
                    if (healthLabel != null && theGame.getCrab() != null) {
                        Platform.runLater(() -> healthLabel.setText(theGame.getCrab().getHp() + "/" + theGame.getCrab().getMaxHp()));
                    } else if (healthLabel != null && theGame.getCrab() == null) {
                        Platform.runLater(() -> healthLabel.setText("0/0"));
                    }
                }
            }
        });

        coconutTimeline = new Timeline(new KeyFrame(Duration.millis(MILLISECONDS_PER_STEP), (e) -> {
            theGame.tryDropCoconut();
            theGame.advanceOneTick();
            if (theGame.done())
                coconutTimeline.pause();
        }));
        coconutTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {
        if (theGame.done() || theGame.getCrab() == null) {
            return;
        }


        if ((keyEvent.getCode() == KeyCode.RIGHT ||
            keyEvent.getCode() == KeyCode.D) && started) {
            theGame.getCrab().crawl(10);
        } else if ((keyEvent.getCode() == KeyCode.LEFT ||
            keyEvent.getCode() == KeyCode.A) && started) {
            theGame.getCrab().crawl(-10);
        } else if (keyEvent.getCode() == KeyCode.SPACE) {
            if (!started) {
                coconutTimeline.play();
                started = true;
            } else {
                coconutTimeline.pause();
                started = false;
            }
        } else if (keyEvent.getCode() == KeyCode.UP && started) {
            theGame.fireLaser();

            shotsFired++;
            if (shotCoconutsLabel != null) {
                shotCoconutsLabel.setText(Integer.toString(shotsFired));
            }
        }
    }
}
