package networking;

import util.DirectionEnum;

public class PositionMessage extends AbstractClientMessage {

    private String address;
    private double xCoord;
    private double yCoord;
    private String mapId;
    private DirectionEnum direction;
    private boolean isWalking;

    public PositionMessage(String address, double xCoord, double yCoord, String mapId, DirectionEnum direction, boolean isWalking) {
        super(MessageTypesEnum.POSITION_UPDATE);
        this.address = address;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.mapId = mapId;
        this.direction = direction;
        this.isWalking = isWalking;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public double getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public DirectionEnum getDirection() {
        return direction;
    }

    public void setDirection(DirectionEnum direction) {
        this.direction = direction;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }
}
