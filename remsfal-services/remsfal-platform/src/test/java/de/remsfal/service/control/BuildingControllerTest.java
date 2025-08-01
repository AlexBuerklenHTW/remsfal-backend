package de.remsfal.service.control;

import de.remsfal.core.json.project.BuildingJson;
import de.remsfal.core.json.project.ImmutableBuildingJson;
import de.remsfal.service.entity.dto.AddressEntity;
import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.remsfal.core.model.project.BuildingModel;
import de.remsfal.core.model.project.PropertyModel;
import de.remsfal.service.AbstractServiceTest;
import de.remsfal.service.entity.dto.BuildingEntity;
import de.remsfal.test.TestData;

@QuarkusTest
class BuildingControllerTest extends AbstractServiceTest {

    @Inject
    PropertyController propertyController;

    @Inject
    BuildingController buildingController;

    @BeforeEach
    void setupTestProjects() {
        runInTransaction(() -> entityManager
            .createNativeQuery("INSERT INTO PROJECT (ID, TITLE) VALUES (?,?)")
            .setParameter(1, TestData.PROJECT_ID_1)
            .setParameter(2, TestData.PROJECT_TITLE_1)
            .executeUpdate());
        runInTransaction(() -> entityManager
            .createNativeQuery("INSERT INTO PROJECT (ID, TITLE) VALUES (?,?)")
            .setParameter(1, TestData.PROJECT_ID_2)
            .setParameter(2, TestData.PROJECT_TITLE_2)
            .executeUpdate());
        runInTransaction(() -> entityManager
            .createNativeQuery("INSERT INTO PROJECT (ID, TITLE) VALUES (?,?)")
            .setParameter(1, TestData.PROJECT_ID_3)
            .setParameter(2, TestData.PROJECT_TITLE_3)
            .executeUpdate());
        runInTransaction(() -> entityManager
            .createNativeQuery("INSERT INTO PROJECT (ID, TITLE) VALUES (?,?)")
            .setParameter(1, TestData.PROJECT_ID_4)
            .setParameter(2, TestData.PROJECT_TITLE_4)
            .executeUpdate());
        runInTransaction(() -> entityManager
            .createNativeQuery("INSERT INTO PROJECT (ID, TITLE) VALUES (?,?)")
            .setParameter(1, TestData.PROJECT_ID_5)
            .setParameter(2, TestData.PROJECT_TITLE_5)
            .executeUpdate());
    }

    @Test
    void createBuilding_FAILED_noProjectNoProperty() {
        final String buildingId = propertyController
            .createProperty(TestData.PROJECT_ID, TestData.propertyBuilder().build())
            .getId();
        assertNotNull(buildingId);

        final BuildingModel building = TestData.buildingBuilder()
        	.id(null)
        	.address(TestData.addressBuilder().build())
            .build();
        
        assertThrows(ConstraintViolationException.class,
            () -> buildingController.createBuilding(null, buildingId, building));
        assertThrows(ConstraintViolationException.class,
            () -> buildingController.createBuilding(TestData.PROJECT_ID, null, building));
    }
    
    @Test
    void createBuilding_SUCCESS_idGenerated() {
        final PropertyModel property = propertyController.createProperty(TestData.PROJECT_ID, TestData.propertyBuilder().build());
        assertNotNull(property.getId());

        final BuildingModel building = TestData.buildingBuilder()
        	.id(null)
            .address(TestData.addressBuilder().build())
            .build();
        
        final BuildingModel result = buildingController.createBuilding(TestData.PROJECT_ID, property.getId(), building);
        
        assertNotEquals(building.getId(), result.getId());
        assertEquals(building.getTitle(), result.getTitle());
        assertEquals(building.getAddress().getStreet(), result.getAddress().getStreet());
        assertEquals(building.getAddress().getCity(), result.getAddress().getCity());
        assertEquals(building.getAddress().getProvince(), result.getAddress().getProvince());
        assertEquals(building.getAddress().getZip(), result.getAddress().getZip());
        assertEquals(building.getDescription(), result.getDescription());
        assertEquals(building.getGrossFloorArea(), result.getGrossFloorArea());
        assertEquals(building.getNetFloorArea(), result.getNetFloorArea());
        assertEquals(building.getConstructionFloorArea(), result.getConstructionFloorArea());
        assertEquals(building.getLivingSpace(), result.getLivingSpace());
        assertEquals(building.getUsableSpace(), result.getUsableSpace());
        
        final BuildingEntity entity = entityManager
            .createQuery("SELECT b FROM BuildingEntity b where b.title = :title", BuildingEntity.class)
            .setParameter("title", TestData.BUILDING_TITLE)
            .getSingleResult();
        assertEquals(entity.hashCode(), result.hashCode());
        assertNotNull(entity.getAddress());
        assertEquals(entity, result);
    }
    
    @Test
    void getBuilding_SUCCESS_buildingRetrieved() {
        assertNotNull(TestData.propertyBuilder().build());
        final PropertyModel property = propertyController.createProperty(TestData.PROJECT_ID, TestData.propertyBuilder().build());
        assertNotNull(property.getId());
        final BuildingModel building = buildingController.createBuilding(TestData.PROJECT_ID, property.getId(),
            TestData.buildingBuilder().id(null).address(TestData.addressBuilder().build()).build());
        assertNotNull(building.getId());

        final BuildingModel result = buildingController.getBuilding(TestData.PROJECT_ID, building.getId());
        
        assertEquals(building.getId(), result.getId());
        assertEquals(building.getTitle(), result.getTitle());
        assertEquals(building.getDescription(), result.getDescription());
        assertEquals(building.getGrossFloorArea(), result.getGrossFloorArea());
        assertEquals(building.getNetFloorArea(), result.getNetFloorArea());
        assertEquals(building.getConstructionFloorArea(), result.getConstructionFloorArea());
        assertEquals(building.getLivingSpace(), result.getLivingSpace());
        assertEquals(building.getUsableSpace(), result.getUsableSpace());
    }
    
    @Test
    void getBuilding_FAILED_wrongProjectId() {
        final String propertyId = propertyController
            .createProperty(TestData.PROJECT_ID_1, TestData.propertyBuilder().build())
            .getId();
        assertNotNull(propertyId);
        final String buildingId = buildingController
            .createBuilding(TestData.PROJECT_ID, propertyId,
            TestData.buildingBuilder().id(null).address(TestData.addressBuilder().build()).build())
            .getId();
        assertNotNull(buildingId);
        
        assertThrows(NotFoundException.class,
            () -> buildingController.getBuilding(TestData.PROJECT_ID_2, buildingId));
    }

    @Test
    void updateBuilding_SUCCESS() {
        assertNotNull(TestData.propertyBuilder().build());
        final PropertyModel property = propertyController.createProperty(TestData.PROJECT_ID, TestData.propertyBuilder().build());
        assertNotNull(property.getId());
        final BuildingModel building = buildingController.createBuilding(TestData.PROJECT_ID, property.getId(),
                TestData.buildingBuilder().id(null).address(TestData.addressBuilder().build()).build());
        assertNotNull(building.getId());

        final BuildingModel result = buildingController.getBuilding(TestData.PROJECT_ID, building.getId());

        AddressEntity address = new AddressEntity();
        address.setCity(result.getAddress().getCity());
        address.setCountry(result.getAddress().getCountry());
        address.setStreet("Lavochkina St.");
        address.setProvince(result.getAddress().getProvince());
        address.setZip(result.getAddress().getZip());

        BuildingModel model = ImmutableBuildingJson.builder()
                .id(result.getId())
                .address(address)
                .title(result.getTitle())
                .description(result.getDescription())
                .heatingSpace(result.getHeatingSpace())
                .livingSpace(result.getLivingSpace())
                .usableSpace(result.getUsableSpace())
                .build();

        BuildingJson updatedBuildingJson = BuildingJson.valueOf(model);

        logger.infov("country: " + updatedBuildingJson.getAddress().getCountry());

        final BuildingModel buildingModel = buildingController.updateBuilding(property.getId(), building.getId(), updatedBuildingJson);

        assertEquals(building.getId(), buildingModel.getId());
        assertEquals(building.getTitle(), buildingModel.getTitle());
        assertEquals(building.getDescription(), buildingModel.getDescription());
        assertEquals(building.getLivingSpace(), buildingModel.getLivingSpace());
        assertEquals(building.getUsableSpace(), buildingModel.getUsableSpace());
        assertEquals(buildingModel.getAddress().getStreet(), "Lavochkina St.");
    }

    @Test
    void updateBuilding_FAILED_wrongBuildingId() {
        assertNotNull(TestData.propertyBuilder().build());
        final PropertyModel property = propertyController.createProperty(TestData.PROJECT_ID, TestData.propertyBuilder().build());
        assertNotNull(property.getId());
        final BuildingModel building = buildingController.createBuilding(TestData.PROJECT_ID, property.getId(),
                TestData.buildingBuilder().id(null).address(TestData.addressBuilder().build()).build());
        assertNotNull(building.getId());

        final BuildingModel result = buildingController.getBuilding(TestData.PROJECT_ID, building.getId());

        AddressEntity address = new AddressEntity();
        address.setCity(result.getAddress().getCity());
        address.setCountry(result.getAddress().getCountry());
        address.setStreet("Lavochkina St.");
        address.setProvince(result.getAddress().getProvince());
        address.setZip(result.getAddress().getZip());

        BuildingModel model = ImmutableBuildingJson.builder()
                .id(result.getId())
                .address(address)
                .title(result.getTitle())
                .description(result.getDescription())
                .heatingSpace(result.getHeatingSpace())
                .livingSpace(result.getLivingSpace())
                .usableSpace(result.getUsableSpace())
                .build();

        BuildingJson updatedBuildingJson = BuildingJson.valueOf(model);

        assertThrows(NotFoundException.class,
                () -> buildingController.updateBuilding(property.getId(), TestData.BUILDING_ID_1, updatedBuildingJson));

    }

    @Test
    void deleteBuilding_SUCCESS() {
        final PropertyModel property = propertyController.createProperty(TestData.PROJECT_ID, TestData.propertyBuilder().build());
        assertNotNull(property.getId());

        final BuildingModel building = TestData.buildingBuilder()
                .id(null)
                .address(TestData.addressBuilder().build())
                .build();

        final BuildingModel result = buildingController.createBuilding(TestData.PROJECT_ID, property.getId(), building);

        buildingController.deleteBuilding(property.getId(), result.getId());

        assertThrows(NotFoundException.class,
                () -> buildingController.getBuilding(TestData.PROJECT_ID, result.getId()));

    }

    @Test
    void deleteBuilding_FAILED() {
        final PropertyModel property = propertyController.createProperty(TestData.PROJECT_ID, TestData.propertyBuilder().build());

        assertNotNull(property.getId());

        final BuildingModel building = TestData.buildingBuilder()
                .id(null)
                .address(TestData.addressBuilder().build())
                .build();

        buildingController.createBuilding(TestData.PROJECT_ID, property.getId(), building);

        buildingController.deleteBuilding(property.getId(), TestData.BUILDING_ID_1);

        Long entity = entityManager
                .createQuery("SELECT count (b) FROM BuildingEntity b where b.title = :title", Long.class)
                .setParameter("title", TestData.BUILDING_TITLE)
                .getSingleResult();
        assertEquals(1, entity);
    }

}
