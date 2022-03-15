package networking;

import java.util.List;

public class HeartbeatServer {

    private List<HeartbeatClient> locationsList;

    public HeartbeatServer(List<HeartbeatClient> locationsList) {
        this.locationsList = locationsList;
    }

    public List<HeartbeatClient> getLocationsList() {
        return locationsList;
    }

    public void setLocationsList(List<HeartbeatClient> locationsList) {
        this.locationsList = locationsList;
    }
}
