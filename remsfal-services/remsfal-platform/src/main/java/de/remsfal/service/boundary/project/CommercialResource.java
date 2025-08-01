package de.remsfal.service.boundary.project;

import de.remsfal.core.api.project.CommercialEndpoint;
import de.remsfal.core.json.project.CommercialJson;
import de.remsfal.core.model.project.CommercialModel;
import de.remsfal.service.control.CommercialController;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

/**
 * @author Alexander Stanik [alexander.stanik@htw-berlin.de]
 *
 * Resource for managing Commercial units via the API.
 */
@RequestScoped
public class CommercialResource extends ProjectSubResource implements CommercialEndpoint {

    @Inject
    CommercialController controller;

    @Override
    public Response createCommercial(final String projectId, final String buildingId,
                                     final CommercialJson commercial) {
        checkWritePermissions(projectId);
        final CommercialModel model = controller.createCommercial(projectId, buildingId, commercial);
        final URI location = uri.getAbsolutePathBuilder().path(model.getId()).build();
        return Response.created(location)
                .type(MediaType.APPLICATION_JSON)
                .entity(CommercialJson.valueOf(model))
                .build();
    }

    @Override
    public CommercialJson getCommercial(final String projectId, final String commercialId) {
        checkReadPermissions(projectId);
        return CommercialJson.valueOf(controller.getCommercial(projectId, commercialId));
    }

    @Override
    public CommercialJson updateCommercial(final String projectId, final String commercialId,
                                           final CommercialJson commercial) {
        checkWritePermissions(projectId);
        return CommercialJson.valueOf(controller.updateCommercial(projectId, commercialId, commercial));
    }

    @Override
    public void deleteCommercial(final String projectId, final String commercialId) {
        checkWritePermissions(projectId);
        controller.deleteCommercial(projectId, commercialId);
    }

}
