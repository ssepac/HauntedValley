package state;


import controls.ActivityPane;
import javafx.scene.layout.StackPane;
import networking.Account;
import org.mapeditor.core.Tile;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

public class AppState {

    private static AppState INSTANCE;
    private double[] playerPos;
    private HashSet<Point> collisionPoints;
    private ActivityPane activityPane;
    private Account account;

    private AppState(){
    }

    public HashSet<Point> getCollisionPoints() {
        return collisionPoints;
    }

    public static AppState getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new AppState();
        }
        return INSTANCE;
    }

    public void setCollisionPoints(HashSet<Point> collisionPoints) {
        this.collisionPoints = collisionPoints;
    }

    public ActivityPane getActivityPane() {
        return activityPane;
    }

    public void setActivityPane(ActivityPane activityPane) {
        this.activityPane = activityPane;
    }

    public double[] getPlayerPos() {
        return playerPos;
    }

    /** Dispatch message to activity pane */
    public void dispatchActivityPaneMessage(String msg){
        activityPane.setVisible(true);
        activityPane.getPrimaryLabel().setText(msg);
    }

    public void setPlayerPos(double[] playerPos) {
        this.playerPos = playerPos;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
