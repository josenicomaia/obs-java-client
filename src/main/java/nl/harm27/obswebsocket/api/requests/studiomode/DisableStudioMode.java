package nl.harm27.obswebsocket.api.requests.studiomode;

import nl.harm27.obswebsocket.api.requests.BaseRequest;
import nl.harm27.obswebsocket.api.requests.BaseResponse;
import nl.harm27.obswebsocket.api.requests.RequestType;

/**
 * Disables Studio Mode.
 *
 * @see <a href="https://github.com/Palakis/obs-websocket/blob/4.x-current/docs/generated/protocol.md#DisableStudioMode">OBS WebSocket Documentation</a>
 * @since v4.1.0
 */
public class DisableStudioMode {
    private DisableStudioMode() {
    }

    public static class Request extends BaseRequest {
        public Request(String messageId) {
            super(RequestType.DISABLE_STUDIO_MODE, messageId);
        }

        @Override
        public Class<?> getResponseType() {
            return Response.class;
        }

        @Override
        public String getRequestName() {
            return DisableStudioMode.class.getSimpleName();
        }
    }

    public static class Response extends BaseResponse {
    }
}