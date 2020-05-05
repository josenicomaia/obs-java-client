package nl.harm27.obswebsocket.listener;

import nl.harm27.obswebsocket.api.events.BaseEvent;
import nl.harm27.obswebsocket.api.events.EventType;
import nl.harm27.obswebsocket.api.events.recording.RecordingStarted;
import nl.harm27.obswebsocket.api.events.recording.RecordingStarting;
import nl.harm27.obswebsocket.api.events.recording.RecordingStopped;
import nl.harm27.obswebsocket.api.events.recording.RecordingStopping;

import java.util.EnumMap;
import java.util.Map;

import static nl.harm27.obswebsocket.api.events.EventType.*;

/**
 * The EventListener for the events that are part of the Recording category.
 */
public abstract class RecordingEventListener implements EventListener {
    @Override
    public final Map<EventType, Class<?>> getSupportedEvents() {
        Map<EventType, Class<?>> supportedEvents = new EnumMap<>(EventType.class);
        supportedEvents.put(RECORDING_STARTING, RecordingStarting.class);
        supportedEvents.put(RECORDING_STARTED, RecordingStarted.class);
        supportedEvents.put(RECORDING_STOPPING, RecordingStopping.class);
        supportedEvents.put(RECORDING_STOPPED, RecordingStopped.class);
        return supportedEvents;
    }

    @Override
    public final void callEvent(BaseEvent baseEvent) {
        switch (baseEvent.getEventType()) {
            case RECORDING_STARTING:
                recordingStarting((RecordingStarting) baseEvent);
                break;
            case RECORDING_STARTED:
                recordingStarted((RecordingStarted) baseEvent);
                break;
            case RECORDING_STOPPING:
                recordingStopping((RecordingStopping) baseEvent);
                break;
            case RECORDING_STOPPED:
                recordingStopped((RecordingStopped) baseEvent);
                break;
            default:
                throw new IllegalStateException("Unexpected EventType for RecordingEventListener.");
        }
    }

    /**
     * Implement this method to process RecordingStarting events.
     *
     * @param recordingStarting The received event.
     */
    public void recordingStarting(RecordingStarting recordingStarting) {
    }

    /**
     * Implement this method to process RecordingStarted events.
     *
     * @param recordingStarted The received event.
     */
    public void recordingStarted(RecordingStarted recordingStarted) {
    }

    /**
     * Implement this method to process RecordingStopping events.
     *
     * @param recordingStopping The received event.
     */
    public void recordingStopping(RecordingStopping recordingStopping) {
    }

    /**
     * Implement this method to process RecordingStopped events.
     *
     * @param recordingStopped The received event.
     */
    public void recordingStopped(RecordingStopped recordingStopped) {
    }
}
