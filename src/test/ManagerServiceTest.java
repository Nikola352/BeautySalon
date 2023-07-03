package test;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Gender;
import entity.LevelOfEducation;
import entity.Manager;
import service.BeautySalonService;
import service.ManagerService;
import service.ServiceRegistry;

public class ManagerServiceTest {
    private static ManagerService managerService;
    private static BeautySalonService beautySalonService;

    @BeforeClass
    public static void setUp() {
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        serviceRegistry.initializeServices();
        managerService = serviceRegistry.getManagerService();
        beautySalonService = serviceRegistry.getBeautySalonService();
    }

    @Before
    public void reset() {
        managerService.getData().clear();
    }

    @Test
    public void testAddManager() {
        assertEquals(0, managerService.getData().size());
        Manager manager = managerService.add(
            "username",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0,
            0.0
        );
        assertEquals(1, managerService.getData().size());
        assertEquals(manager, managerService.getData().get(0));
    }

    @Test
    public void testRemoveManager(){
        assertEquals(0, managerService.getData().size());
        Manager manager = managerService.add(
            "username",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress",
            LevelOfEducation.BACHELOR,
            0,
            10000.0,
            0.0
        );
        assertEquals(1, managerService.getData().size());
        managerService.remove(manager);
        assertEquals(0, managerService.getData().size());
        assertFalse(managerService.getData().contains(manager));
    }

    @Test
    public void testGetManagerByUsername(){
        Manager manager1 = managerService.add(
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
            0.0
        );
        Manager manager2 = managerService.add(
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
            0.0
        );
        assertEquals(manager1, managerService.getByUsername("username1"));
        assertEquals(manager2, managerService.getByUsername("username2"));
    }

    @Test
    public void testGetTotalSalaryForTimePeriod(){
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 4, 30);
        double totalSalary = 0.0;
        for(Manager manager : managerService.getData()){
            totalSalary += 4 * manager.getSalary();
        }
        assertEquals(totalSalary, managerService.getTotalSalaryForTimePeriod(startDate, endDate), 0.001);
    }

    @Test
    public void testGetTotalSalaryForTimePeriodWithNoManagers(){
        managerService.getData().clear();
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 4, 30);
        assertEquals(0.0, managerService.getTotalSalaryForTimePeriod(startDate, endDate), 0.001);
    }

    @Test
    public void testUpdateBonus(){
        double bonusThreashold = beautySalonService.getBeautySalon().getBonusThreshold();
        double bonusAmount = beautySalonService.getBeautySalon().getBonusAmount();
        Manager manager1 = managerService.add(
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
            0.0
        );
        Manager manager2 = managerService.add(
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
            0.0
        ); 
        manager2.setCurrentMonthIncome(bonusAmount);
        managerService.updateBonus(bonusThreashold, bonusAmount);
        assertEquals(0.0, manager1.getBonus(), 0.001);
        assertEquals(bonusAmount, manager2.getBonus(), 0.001);
    }

}
