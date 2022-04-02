package state;


import components.sprites.ForeignSprite;
import controls.ActivityPane;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import networking.Account;
import networking.ServerMessage;
import util.DirectionEnum;

import java.awt.*;
import java.util.*;

public class AppState {

    //needed to add new sprites
    private StackPane sceneRoot;
    private HashMap<String, ServerMessage> playerCoordsMap;
    private HashMap<String, ForeignSprite> sprites;

    private static AppState INSTANCE;
    private double[] playerPos;
    private HashSet<Point> collisionPoints;
    private ActivityPane activityPane;
    private Account account;
    private boolean spriteWalking;
    private DirectionEnum spriteDirFacing;
    private String activeMap;

    private AppState(){
    }

    public HashSet<Point> getCollisionPoints() {
        return collisionPoints;
    }

    public static AppState getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new AppState();
            INSTANCE.playerCoordsMap = new HashMap<>();
            INSTANCE.sprites = new HashMap<>();
        }
        return INSTANCE;
    }

    public DirectionEnum getSpriteDirFacing() {
        return spriteDirFacing;
    }

    public void setSpriteDirFacing(DirectionEnum spriteDirFacing) {
        this.spriteDirFacing = spriteDirFacing;
    }

    public boolean getSpriteWalking() {
        return spriteWalking;
    }

    public void setSpriteWalking(boolean spriteWalking) {
        this.spriteWalking = spriteWalking;
    }

    public String getActiveMap() {
        return activeMap;
    }

    public void setActiveMap(String activeMap) {
        this.activeMap = activeMap;
    }

    public void setCollisionPoints(HashSet<Point> collisionPoints) {
        this.collisionPoints = collisionPoints;
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

    public void setSceneRoot(StackPane sceneRoot) {
        this.sceneRoot = sceneRoot;
    }

    //Called when a broadcast is received from the server.
    public void setPlayerCoordsMap(HashMap<String, ServerMessage> map){

        for(Map.Entry<String, ServerMessage> entry : map.entrySet()){
            //Add player to map if client seeing them for first time, with the exclusion of oneself.
            if(!playerCoordsMap.containsKey(entry.getKey()) && !Objects.equals(entry.getKey(), account.getAddress().getValue())){
                ForeignSprite newForeignSprite = ForeignSprite.generateForeignSprite(entry.getKey());
                sprites.put(entry.getKey(), newForeignSprite);
                Platform.runLater(()->sceneRoot.getChildren().add(newForeignSprite.getImageView()));
            }
            playerCoordsMap.put(entry.getKey(), entry.getValue());
        }
    }

    public ServerMessage getPlayerServerPosition(String id){
        return playerCoordsMap.get(id);
    }
}
