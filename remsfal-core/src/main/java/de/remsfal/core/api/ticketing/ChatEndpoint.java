package de.remsfal.core.api.ticketing;

import jakarta.ws.rs.Path;

import de.remsfal.core.api.project.TaskEndpoint;

/**
 * @author: Alexander Stanik [alexander.stanik@htw-berlin.de]
 */
@Path(ChatEndpoint.CONTEXT + "/" + ChatEndpoint.VERSION + "/" + ChatEndpoint.SERVICE)
public interface ChatEndpoint {

    String CONTEXT = "api";
    String VERSION = "v1";
    String SERVICE = "projects";

    @Path("/{projectId}/" + TaskEndpoint.SERVICE + "/{taskId}/" + ChatSessionEndpoint.SERVICE)
    ChatSessionEndpoint getChatSessionResource();

}
