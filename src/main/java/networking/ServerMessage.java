package networking;

public class ServerMessage {
    private double x;
    private double y;
    private String mapId;
    private String direction;
    private MessageTypesEnum messageType;
    private boolean isWalking;

    public ServerMessage(double x, double y, String mapId, String direction, MessageTypesEnum messageType, boolean isWalking) {
        this.x = x;
        this.y = y;
        this.mapId = mapId;
        this.direction = direction;
        this.messageType = messageType;
        this.isWalking = isWalking;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public MessageTypesEnum getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageTypesEnum messageType) {
        this.messageType = messageType;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    @Override
    public String toString() {
        return "ServerPositionBroadcast{" +
                "x=" + x +
                ", y=" + y +
                ", mapId='" + mapId + '\'' +
                ", direction='" + direction + '\'' +
                ", messageType=" + messageType +
                ", isWalking=" + isWalking +
                '}';
    }
}