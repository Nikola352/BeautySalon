package test;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Gender;
import entity.LevelOfEducation;
import entity.Receptionist;
import service.BeautySalonService;
import service.ReceptionistService;
import service.ServiceRegistry;

public class ReceptionistServiceTest {
    private static ReceptionistService receptionistService;
    private static BeautySalonService beautySalonService;

    @BeforeClass
    public static void setUp() {
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        serviceRegistry.initializeServices();
        receptionistService = serviceRegistry.getReceptionistService();
        beautySalonService = serviceRegistry.getBeautySalonService();
    }

    @Before
    public void reset() {
        receptionistService.getData().clear();
    }

    @Test
    public void testAddReceptionist() {
        assertEquals(0, receptionistService.getData().size());
        Receptionist receptionist = receptionistService.add(
            "username",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0
        );
        assertEquals(1, receptionistService.getData().size());
        assertEquals(receptionist, receptionistService.getData().get(0));
    }

    @Test
    public void testRemoveReceptionist(){
        assertEquals(0, receptionistService.getData().size());
        Receptionist receptionist = receptionistService.add(
            "username",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0
        );
        assertEquals(1, receptionistService.getData().size());
        receptionistService.remove(receptionist);
        assertEquals(0, receptionistService.getData().size());
        assertFalse(receptionistService.getData().contains(receptionist));
    }

    @Test
    public void testGetReceptionistByUsername(){
        Receptionist receptionist1 = receptionistService.add(
            "username1",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0
        );
        Receptionist receptionist2 = receptionistService.add(
            "username2",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0
        );
        assertEquals(receptionist1, receptionistService.getByUsername("username1"));
        assertEquals(receptionist2, receptionistService.getByUsername("username2"));
    }

    @Test
    public void testGetTotalSalaryForTimePeriod(){
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 4, 30);
        double totalSalary = 0.0;
        for(Receptionist receptionist : receptionistService.getData()){
            totalSalary += 4 * receptionist.getSalary();
        }
        assertEquals(totalSalary, receptionistService.getTotalSalaryForTimePeriod(startDate, endDate), 0.001);
    }

    @Test
    public void testGetTotalSalaryForTimePeriodWithNoReceptionists(){
        receptionistService.getData().clear();
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 4, 30);
        assertEquals(0.0, receptionistService.getTotalSalaryForTimePeriod(startDate, endDate), 0.001);
    }

    @Test
    public void testUpdateBonus(){
        double bonusThreashold = beautySalonService.getBeautySalon().getBonusThreshold();
        double bonusAmount = beautySalonService.getBeautySalon().getBonusAmount();
        Receptionist receptionist1 = receptionistService.add(
            "username1",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0
        );
        Receptionist receptionist2 = receptionistService.add(
            "username2",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0
        ); 
        receptionist2.setCurrentMonthIncome(bonusAmount);
        receptionistService.updateBonus(bonusThreashold, bonusAmount);
        assertEquals(0.0, receptionist1.getBonus(), 0.001);
        assertEquals(bonusAmount, receptionist2.getBonus(), 0.001);
    }

}
