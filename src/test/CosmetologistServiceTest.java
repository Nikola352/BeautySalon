package test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Gender;
import entity.LevelOfEducation;
import entity.TreatmentType;
import entity.Cosmetologist;
import service.BeautySalonService;
import service.CosmetologistService;
import service.ServiceRegistry;
import service.TreatmentTypeService;

public class CosmetologistServiceTest {
    private static CosmetologistService cosmetologistService;
    private static BeautySalonService beautySalonService;
    private static TreatmentTypeService treatmentTypeService;

    @BeforeClass
    public static void setUp() {
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        serviceRegistry.initializeServices();
        cosmetologistService = serviceRegistry.getCosmetologistService();
        beautySalonService = serviceRegistry.getBeautySalonService();
        treatmentTypeService = serviceRegistry.getTreatmentTypeService();

        treatmentTypeService.add("treatment_type1", "description");
        treatmentTypeService.add("treatment_type2", "description");
        treatmentTypeService.add("treatment_type3", "description");
    }

    @Before
    public void reset() {
        cosmetologistService.getData().clear();
    }

    @Test
    public void testAddCosmetologist(){
        assertEquals(0, cosmetologistService.getData().size());
        Cosmetologist cosmetologist = cosmetologistService.add(
            "username",
            "password",
            "name",
            "lastname",
            Gender.FEMALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0,
            0.0,
            treatmentTypeService.getData()
        );
        assertEquals(1, cosmetologistService.getData().size());
        assertEquals(cosmetologist, cosmetologistService.getData().get(0));
    }

    @Test
    public void testRemoveCosmetologist(){
        assertEquals(0, cosmetologistService.getData().size());
        Cosmetologist cosmetologist = cosmetologistService.add(
            "username",
            "password",
            "name",
            "lastname",
            Gender.FEMALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0,
            0.0,
            treatmentTypeService.getData()
        );
        assertEquals(1, cosmetologistService.getData().size());
        cosmetologistService.remove(cosmetologist);
        assertEquals(0, cosmetologistService.getData().size());
        assertFalse(cosmetologistService.getData().contains(cosmetologist));
    }

    @Test
    public void testGetReceptionistByUsername(){
        Cosmetologist cosmetologist1 = cosmetologistService.add(
            "username1",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0,
            0.0,
            treatmentTypeService.getData()
        );
        Cosmetologist cosmetologist2 = cosmetologistService.add(
            "username2",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0,
            0.0,
            treatmentTypeService.getData()
        );
        assertEquals(cosmetologist1, cosmetologistService.getByUsername("username1"));
        assertEquals(cosmetologist2, cosmetologistService.getByUsername("username2"));
    }

    @Test
    public void testGetTotalSalaryForTimePeriod(){
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 4, 30);
        double totalSalary = 0.0;
        for(Cosmetologist receptionist : cosmetologistService.getData()){
            totalSalary += 4 * receptionist.getSalary();
        }
        assertEquals(totalSalary, cosmetologistService.getTotalSalaryForTimePeriod(startDate, endDate), 0.001);
    }

    @Test
    public void testGetTotalSalaryForTimePeriodWithNoCosmetologists(){
        cosmetologistService.getData().clear();
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 4, 30);
        assertEquals(0.0, cosmetologistService.getTotalSalaryForTimePeriod(startDate, endDate), 0.001);
    }

    @Test
    public void testUpdateBonus(){
        double bonusThreashold = beautySalonService.getBeautySalon().getBonusThreshold();
        double bonusAmount = beautySalonService.getBeautySalon().getBonusAmount();
        Cosmetologist cosmetologist1 = cosmetologistService.add(
            "username1",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0,
            0.0,
            treatmentTypeService.getData()
        );
        Cosmetologist cosmetologist2 = cosmetologistService.add(
            "username2",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0,
            0.0,
            treatmentTypeService.getData()
        );
        cosmetologist2.setCurrentMonthIncome(bonusAmount);
        cosmetologistService.updateBonus(bonusThreashold, bonusAmount);
        assertEquals(0.0, cosmetologist1.getBonus(), 0.001);
        assertEquals(bonusAmount, cosmetologist2.getBonus(), 0.001);
    }

    @Test
    public void testGetCosmetologistByTreatmentType(){
        ArrayList<TreatmentType> treatmentTypes1 = treatmentTypeService.getData();
        Cosmetologist cosmetologist1 = cosmetologistService.add(
            "username1",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0,
            0.0,
            treatmentTypes1
        );
        ArrayList<TreatmentType> treatmentTypes2 = new ArrayList<TreatmentType>(treatmentTypeService.getData().subList(0, 1));
        cosmetologistService.add(
            "username2",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0,
            0.0,
            treatmentTypes2
        );
        assertEquals(2, cosmetologistService.getByTreatmentType(treatmentTypes1.get(0)).size());
        assertEquals(cosmetologistService.getData(), cosmetologistService.getByTreatmentType(treatmentTypes1.get(0)));
        assertEquals(1, cosmetologistService.getByTreatmentType(treatmentTypes1.get(1)).size());
        assertEquals(cosmetologist1, cosmetologistService.getByTreatmentType(treatmentTypes2.get(0)).get(0));
    }
}
