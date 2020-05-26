package nl.harm27.obswebsocket.api.requests.general;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.harm27.obswebsocket.api.requests.BaseRequest;
import nl.harm27.obswebsocket.api.requests.BaseResponse;
import nl.harm27.obswebsocket.api.requests.RequestType;

import java.util.Arrays;
import java.util.List;

/**
 * Returns the latest version of the plugin and the API.
 *
 * @see <a href="https://github.com/Palakis/obs-websocket/blob/4.x-current/docs/generated/protocol.md#getversion">OBS WebSocket Documentation</a>
 * @since v0.3
 */
public class GetVersion {
    private GetVersion() {
    }

    public static class Request extends BaseRequest {
        public Request(String messageId) {
            super(RequestType.GET_VERSION, messageId);
        }

        @Override
        public Class<?> getResponseType() {
            return Response.class;
        }

        @Override
        public boolean isAuthenticationRequired() {
            return false;
        }

        @Override
        public String getRequestName() {
            return GetVersion.class.getSimpleName();
        }
    }

    public static class Response extends BaseResponse {
        @JsonProperty("version")
        private double version;
        @JsonProperty("obs-websocket-version")
        private String obsWebsocketVersion;
        @JsonProperty("obs-studio-version")
        private String obsStudioVersion;
        @JsonProperty("available-requests")
        private String availableRequests;

        /**
         * OBSRemote compatible API version. Fixed to 1.1 for retrocompatibility.
         */
        public double getVersion() {
            return version;
        }

        /**
         * OBS Websocket plugin version.
         */
        public String getObsWebsocketVersion() {
            return obsWebsocketVersion;
        }

        /**
         * OBS Studio program version.
         */
        public String getObsStudioVersion() {
            return obsStudioVersion;
        }

        /**
         * List of available request types, formatted as a comma-separated list string (e.g. : "Method1,Method2,Method3").
         */
        public String getAvailableRequests() {
            return availableRequests;
        }

        /**
         * List of available request types.
         */
        public List<String> getAvailableRequestsAsList() {
            return Arrays.asList(availableRequests.split(","));
        }
    }
}