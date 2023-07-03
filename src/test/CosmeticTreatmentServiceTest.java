package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.CosmeticTreatment;
import entity.TreatmentType;
import service.CosmeticTreatmentService;
import service.ServiceRegistry;
import service.TreatmentTypeService;

public class CosmeticTreatmentServiceTest {
    private static CosmeticTreatmentService cosmeticTreatmentService;
    private static TreatmentTypeService treatmentTypeService;
    private static TreatmentType treatmentType1;
    private static TreatmentType treatmentType2;

    @BeforeClass
    public static void setUp() {
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        serviceRegistry.initializeServices();
        cosmeticTreatmentService = serviceRegistry.getCosmeticTreatmentService();
        treatmentTypeService = serviceRegistry.getTreatmentTypeService();
        treatmentType1 = treatmentTypeService.add("treatment_type1", "description");
        treatmentType2 = treatmentTypeService.add("treatment_type2", "description");
    }

    @Before
    public void reset() {
        cosmeticTreatmentService.getData().clear();
    }

    @Test
    public void testAddCosmeticTreatment() {
        assertEquals(0, cosmeticTreatmentService.getData().size());
        CosmeticTreatment cosmeticTreatment = cosmeticTreatmentService.add(
            "name",
            treatmentType1,
            100.0,
            30
        );
        assertEquals(1, cosmeticTreatmentService.getData().size());
        assertEquals(cosmeticTreatment, cosmeticTreatmentService.getData().get(0));
    }

    @Test
    public void testRemoveCosmeticTreatment(){
        assertEquals(0, cosmeticTreatmentService.getData().size());
        CosmeticTreatment cosmeticTreatment = cosmeticTreatmentService.add(
            "name",
            treatmentType1,
            100.0,
            30
        );
        assertEquals(1, cosmeticTreatmentService.getData().size());
        cosmeticTreatmentService.remove(cosmeticTreatment);
        assertEquals(0, cosmeticTreatmentService.getData().size());
    }

    @Test
    public void testGetCosmeticTreatmentByName(){
        assertEquals(0, cosmeticTreatmentService.getData().size());
        CosmeticTreatment cosmeticTreatment = cosmeticTreatmentService.add(
            "name",
            treatmentType1,
            100.0,
            30
        );
        assertEquals(1, cosmeticTreatmentService.getData().size());
        assertEquals(cosmeticTreatment, cosmeticTreatmentService.getBy("name", null, -1, -1).get(0));
    }

    @Test
    public void testGetCosmeticTreatmentByTreatmentType(){
        assertEquals(0, cosmeticTreatmentService.getData().size());
        CosmeticTreatment cosmeticTreatment1 = cosmeticTreatmentService.add(
            "name1",
            treatmentType1,
            100.0,
            30
        );
        CosmeticTreatment cosmeticTreatment2 = cosmeticTreatmentService.add(
            "name2",
            treatmentType2,
            100.0,
            30
        );
        assertEquals(2, cosmeticTreatmentService.getData().size());
        assertEquals(cosmeticTreatment1, cosmeticTreatmentService.getBy(null, treatmentType1, -1, -1).get(0));
        assertEquals(cosmeticTreatment2, cosmeticTreatmentService.getBy(null, treatmentType2, -1, -1).get(0));
    }

    @Test
    public void testGetCosmeticTreatmentByPrice(){
        assertEquals(0, cosmeticTreatmentService.getData().size());
        CosmeticTreatment cosmeticTreatment1 = cosmeticTreatmentService.add(
            "name1",
            treatmentType1,
            100.0,
            30
        );
        CosmeticTreatment cosmeticTreatment2 = cosmeticTreatmentService.add(
            "name2",
            treatmentType2,
            200.0,
            30
        );
        assertEquals(2, cosmeticTreatmentService.getData().size());
        assertEquals(cosmeticTreatment1, cosmeticTreatmentService.getBy(null, null, 100.0, -1).get(0));
        assertEquals(cosmeticTreatment2, cosmeticTreatmentService.getBy(null, null, 200.0, -1).get(0));
    }

    @Test
    public void testGetCosmeticTreatmentByDuration(){
        assertEquals(0, cosmeticTreatmentService.getData().size());
        CosmeticTreatment cosmeticTreatment1 = cosmeticTreatmentService.add(
            "name1",
            treatmentType1,
            100.0,
            30
        );
        CosmeticTreatment cosmeticTreatment2 = cosmeticTreatmentService.add(
            "name2",
            treatmentType2,
            100.0,
            60
        );
        assertEquals(2, cosmeticTreatmentService.getData().size());
        assertEquals(cosmeticTreatment1, cosmeticTreatmentService.getBy(null, null, -1, 30).get(0));
        assertEquals(cosmeticTreatment2, cosmeticTreatmentService.getBy(null, null, -1, 60).get(0));
    }

    @Test
    public void testGetCosmeticTreatmentByAll(){
        assertEquals(0, cosmeticTreatmentService.getData().size());
        CosmeticTreatment cosmeticTreatment1 = cosmeticTreatmentService.add(
            "name1",
            treatmentType1,
            100.0,
            30
        );
        CosmeticTreatment cosmeticTreatment2 = cosmeticTreatmentService.add(
            "name2",
            treatmentType2,
            200.0,
            60
        );
        assertEquals(2, cosmeticTreatmentService.getData().size());
        assertEquals(cosmeticTreatment1, cosmeticTreatmentService.getBy("name1", treatmentType1, 100.0, 30).get(0));
        assertEquals(cosmeticTreatment2, cosmeticTreatmentService.getBy("name2", treatmentType2, 200.0, 60).get(0));
    }

    @Test
    public void testGetCosmeticTreatmentByAllWithNoResult(){
        assertEquals(0, cosmeticTreatmentService.getData().size());
        cosmeticTreatmentService.add(
            "name1",
            treatmentType1,
            100.0,
            30
        );
        cosmeticTreatmentService.add(
            "name2",
            treatmentType2,
            200.0,
            60
        );
        assertEquals(2, cosmeticTreatmentService.getData().size());
        assertEquals(0, cosmeticTreatmentService.getBy("name1", treatmentType2, 100.0, 30).size());
        assertEquals(0, cosmeticTreatmentService.getBy("name2", treatmentType1, 200.0, 60).size());
    }

    @Test
    public void testGetCosmeticTreatmentByAllWithMultipleResult(){
        assertEquals(0, cosmeticTreatmentService.getData().size());
        CosmeticTreatment cosmeticTreatment1 = cosmeticTreatmentService.add(
            "name1",
            treatmentType1,
            100.0,
            30
        );
        CosmeticTreatment cosmeticTreatment2 = cosmeticTreatmentService.add(
            "name1",
            treatmentType2,
            200.0,
            60
        );
        assertEquals(2, cosmeticTreatmentService.getData().size());
        assertEquals(2, cosmeticTreatmentService.getBy("name1", null, -1, -1).size());
        ArrayList<CosmeticTreatment> cosmeticTreatments = new ArrayList<>();
        cosmeticTreatments.add(cosmeticTreatment1);
        cosmeticTreatments.add(cosmeticTreatment2);
        assertEquals(cosmeticTreatments, cosmeticTreatmentService.getBy("name1", null, -1, -1));
    }

    @Test
    public void testUpdateCosmeticTreatment(){
        assertEquals(0, cosmeticTreatmentService.getData().size());
        CosmeticTreatment cosmeticTreatment = cosmeticTreatmentService.add(
            "name",
            treatmentType1,
            100.0,
            30
        );
        assertEquals(1, cosmeticTreatmentService.getData().size());
        assertEquals(cosmeticTreatment, cosmeticTreatmentService.getData().get(0));
        cosmeticTreatmentService.update(
            cosmeticTreatment,
            "newName",
            treatmentType2,
            200.0,
            60
        );
        assertEquals(1, cosmeticTreatmentService.getData().size());
        cosmeticTreatment.setName("newName");
        assertEquals(cosmeticTreatment, cosmeticTreatmentService.getData().get(0));
    }

}
