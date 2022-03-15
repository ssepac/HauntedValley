package networking;

public class HeartbeatClient {

    private String address;
    private double xCoord;
    private double yCoord;

    public HeartbeatClient(String address, double xCoord, double yCoord) {
        this.address = address;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
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
}
