package networking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

public abstract class AbstractClientMessage {
    protected MessageTypesEnum messageType;

    public AbstractClientMessage(MessageTypesEnum messageTypesEnum){
        this.messageType = messageTypesEnum;
    }

    public MessageTypesEnum getMessageType() {
        return messageType;
    }

}
