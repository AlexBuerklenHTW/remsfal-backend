package de.remsfal.core.json.project;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import de.remsfal.core.ImmutableStyle;
import de.remsfal.core.model.project.TaskModel;
import de.remsfal.core.validation.NullOrNotBlank;
import de.remsfal.core.validation.PostValidation;
import de.remsfal.core.validation.UUID;

/**
 * @author Alexander Stanik [alexander.stanik@htw-berlin.de]
 */
@Immutable
@ImmutableStyle
@Schema(description = "A task")
@JsonDeserialize(as = ImmutableTaskJson.class)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public abstract class TaskJson implements TaskModel {

    @Null
    @Nullable
    @Override
    public abstract String getId();

    @Null
    @Nullable
    @Override
    public abstract String getProjectId();

    @NullOrNotBlank
    @NotBlank(groups = PostValidation.class)
    @Size(max = 255)
    @Nullable
    @Override
    public abstract String getTitle();

    @Nullable
    @Override
    public abstract Type getType();

    @Nullable
    @Override
    public abstract Status getStatus();

    @UUID
    @Nullable
    @Override
    public abstract String getOwnerId();

    @Nullable
    @Override
    public abstract String getDescription();

    @UUID
    @Nullable
    @Override
    public abstract String getBlockedBy();

    @UUID
    @Nullable
    @Override
    public abstract String getRelatedTo();

    @UUID
    @Nullable
    @Override
    public abstract String getDuplicateOf();

    public static TaskJson valueOf(final TaskModel model) {
        return ImmutableTaskJson.builder()
                .id(model.getId())
                .projectId(model.getProjectId())
                .title(model.getTitle())
                .type(model.getType())
                .status(model.getStatus())
                .ownerId(model.getOwnerId())
                .description(model.getDescription())
                .blockedBy(model.getBlockedBy())
                .relatedTo(model.getRelatedTo())
                .duplicateOf(model.getDuplicateOf())
                .build();
    }

}
