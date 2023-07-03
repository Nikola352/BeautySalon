package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.TreatmentType;
import service.ServiceRegistry;
import service.TreatmentTypeService;


public class TreatmentTypeServiceTest {
    private static TreatmentTypeService treatmentTypeService;

    @BeforeClass
    public static void setUp() {
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        serviceRegistry.initializeServices();
        treatmentTypeService = serviceRegistry.getTreatmentTypeService();
    }

    @Before
    public void reset() {
        treatmentTypeService.getData().clear();
    }

    @Test
    public void testAddTreatmentType() {
        assertEquals(0, treatmentTypeService.getData().size());
        TreatmentType treatmentType = treatmentTypeService.add("name", "description");
        assertEquals(1, treatmentTypeService.getData().size());
        assertEquals(treatmentType, treatmentTypeService.getData().get(0));
    }

    @Test
    public void testRemoveTreatmentType(){
        assertEquals(0, treatmentTypeService.getData().size());
        TreatmentType treatmentType = treatmentTypeService.add("name", "description");
        assertEquals(1, treatmentTypeService.getData().size());
        treatmentTypeService.remove(treatmentType);
        assertEquals(0, treatmentTypeService.getData().size());
    }

    @Test
    public void testGetTreatmentTypeByName(){
        assertEquals(0, treatmentTypeService.getData().size());
        TreatmentType treatmentType = treatmentTypeService.add("name", "description");
        assertEquals(1, treatmentTypeService.getData().size());
        assertEquals(treatmentType, treatmentTypeService.getByName("name"));
    }
}
