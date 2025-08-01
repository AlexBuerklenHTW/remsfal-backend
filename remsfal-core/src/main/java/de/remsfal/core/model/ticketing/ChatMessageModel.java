package de.remsfal.core.model.ticketing;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a chat message in Cassandra.
 */
public interface ChatMessageModel {

    UUID getChatSessionId();

    UUID getMessageId();

    UUID getSenderId();

    String getContentType();

    String getContent();

    String getUrl();

    Instant getCreatedAt();
}
