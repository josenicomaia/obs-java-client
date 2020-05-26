package nl.harm27.obswebsocket.api.events.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.harm27.obswebsocket.api.events.BaseEvent;

import java.util.Map;

/**
 * A custom broadcast message was received
 *
 * @see <a href="https://github.com/Palakis/obs-websocket/blob/4.x-current/docs/generated/protocol.md#BroadcastCustomMessage">OBS WebSocket Documentation</a>
 * @since v4.7.0
 */
public class BroadcastCustomMessage extends BaseEvent {
    @JsonProperty("realm")
    private String realm;
    @JsonProperty("data")
    private Map<String, Object> data;

    /**
     * Identifier provided by the sender
     */
    public String getRealm() {
        return realm;
    }

    /**
     * User-defined data
     */
    public Map<String, Object> getData() {
        return data;
    }
}