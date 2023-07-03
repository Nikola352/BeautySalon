package test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Appointment;
import entity.AppointmentStatus;
import entity.Client;
import entity.CosmeticTreatment;
import entity.Cosmetologist;
import entity.Gender;
import entity.LevelOfEducation;
import entity.TreatmentType;
import service.AppointmentService;
import service.PriceListService;
import service.ServiceRegistry;

public class AppointmentServiceTest {
    private static AppointmentService appointmentService;
    private static PriceListService priceListService;

    private static CosmeticTreatment cosmeticTreatment1;
    private static CosmeticTreatment cosmeticTreatment2;
    private static Client client1;
    private static Client client2;
    private static Cosmetologist cosmetologist1;
    private static Cosmetologist cosmetologist2;
    private static TreatmentType treatmentType1;
    private static TreatmentType treatmentType2;

    @BeforeClass
    public static void setUp() {
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        serviceRegistry.initializeServices();
        appointmentService = serviceRegistry.getAppointmentService();
        priceListService = serviceRegistry.getPriceListService();

        treatmentType1 = new TreatmentType(1, "treatmentType1", "description");
        treatmentType2 = new TreatmentType(2, "treatmentType2", "description");
        cosmeticTreatment1 = new CosmeticTreatment(1, "cosmeticTreatment1", treatmentType1, 30);
        cosmeticTreatment2 = new CosmeticTreatment(2, "cosmeticTreatment2", treatmentType1, 60);
        priceListService.add(1, 1000);
        priceListService.add(2, 2000);
        client1 = new Client(1, "client1", "pass", "name", "lastname", Gender.MALE, "06666666", "address");
        client2 = new Client(2, "client2", "pass", "name", "lastname", Gender.MALE, "06666666", "address");
        ArrayList<TreatmentType> treatmentTypesList = new ArrayList<TreatmentType>();
        treatmentTypesList.add(treatmentType1);
        treatmentTypesList.add(treatmentType2);
        cosmetologist1 = new Cosmetologist(1, "cosmetologist1", "pass", "name", "lastname", Gender.FEMALE, "06543210", "address", LevelOfEducation.BACHELOR, 3, 100, 0, treatmentTypesList);
        cosmetologist2 = new Cosmetologist(2, "cosmetologist1", "pass", "name", "lastname", Gender.FEMALE, "06543210", "address", LevelOfEducation.BACHELOR, 3, 100, 0, treatmentTypesList);
    }

    @Before
    public void reset() {
        appointmentService.getData().clear();
    }

    @Test
    public void testAddAppointment() {
        assertEquals(0, appointmentService.getData().size());
        Appointment appointment = appointmentService.add(
            cosmeticTreatment1,
            cosmetologist1, 
            client1, 
            LocalDate.now(),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        assertEquals(1, appointmentService.getData().size());
        assertEquals(appointment, appointmentService.getData().get(0));
    }

    @Test
    public void testRemoveAppointment(){
        assertEquals(0, appointmentService.getData().size());
        Appointment appointment = appointmentService.add(
            cosmeticTreatment1,
            cosmetologist1, 
            client1, 
            LocalDate.now(),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        assertEquals(1, appointmentService.getData().size());
        appointmentService.remove(appointment);
        assertEquals(0, appointmentService.getData().size());
    }

    @Test
    public void testScheduleAppointment(){
        assertEquals(0, appointmentService.getData().size());
        Appointment appointment = appointmentService.schedule(cosmeticTreatment1, cosmetologist1, client1, LocalDate.now(), LocalTime.of(12, 0));
        assertEquals(AppointmentStatus.SCHEDULED, appointment.getStatus());
        assertEquals(1, appointmentService.getData().size());
    }

    @Test
    public void testCancelAppointmentAsClient(){
        assertEquals(0, appointmentService.getData().size());
        Appointment appointment = appointmentService.add(
            cosmeticTreatment1,
            cosmetologist1, 
            client1, 
            LocalDate.now(),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        assertEquals(1, appointmentService.getData().size());
        appointmentService.cancel(appointment, client1);
        assertEquals(AppointmentStatus.CANCELED_BY_CLIENT, appointment.getStatus());
        assertEquals(1, appointmentService.getData().size());
    }

    @Test
    public void testCancelAppointmentAsCosmetologist(){
        assertEquals(0, appointmentService.getData().size());
        Appointment appointment = appointmentService.add(
            cosmeticTreatment1,
            cosmetologist1, 
            client1, 
            LocalDate.now(),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        assertEquals(1, appointmentService.getData().size());
        appointmentService.cancel(appointment, cosmetologist1);
        assertEquals(AppointmentStatus.CANCELED_BY_SALON, appointment.getStatus());
        assertEquals(1, appointmentService.getData().size());
    }

    @Test
    public void testGetAppointmentByCosmeticTreatment(){
        assertEquals(0, appointmentService.getData().size());
        Appointment appointment1 = appointmentService.add(
            cosmeticTreatment1,
            cosmetologist1, 
            client1, 
            LocalDate.now(),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        Appointment appointment2 = appointmentService.add(
            cosmeticTreatment1,
            cosmetologist2, 
            client2, 
            LocalDate.now(),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        assertEquals(2, appointmentService.getData().size());
        assertEquals(2, appointmentService.getByCosmeticTreatment(cosmeticTreatment1).size());
        assertEquals(appointment1, appointmentService.getByCosmeticTreatment(cosmeticTreatment1).get(0));
        assertEquals(appointment2, appointmentService.getByCosmeticTreatment(cosmeticTreatment1).get(1));
    }

    @Test
    public void testGetAppointmentByCosmetologist(){
        assertEquals(0, appointmentService.getData().size());
        Appointment appointment1 = appointmentService.add(
            cosmeticTreatment1,
            cosmetologist1, 
            client1, 
            LocalDate.now(),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        Appointment appointment2 = appointmentService.add(
            cosmeticTreatment2,
            cosmetologist1, 
            client2, 
            LocalDate.now(),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        assertEquals(2, appointmentService.getData().size());
        assertEquals(2, appointmentService.getByCosmetologist(cosmetologist1).size());
        assertEquals(appointment1, appointmentService.getByCosmetologist(cosmetologist1).get(0));
        assertEquals(appointment2, appointmentService.getByCosmetologist(cosmetologist1).get(1));
    }

    @Test
    public void testGetAppointmentByCosmetologistAndStatus(){
        assertEquals(0, appointmentService.getData().size());
        Appointment appointment1 = appointmentService.add(
            cosmeticTreatment1,
            cosmetologist1, 
            client1, 
            LocalDate.now(),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.COMPLETED
        );
        appointmentService.add(
            cosmeticTreatment2,
            cosmetologist1, 
            client2, 
            LocalDate.now(),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        assertEquals(2, appointmentService.getData().size());
        assertEquals(1, appointmentService.getByCosmetologistAndStatus(cosmetologist1, AppointmentStatus.COMPLETED).size());
        assertEquals(appointment1, appointmentService.getByCosmetologistAndStatus(cosmetologist1, AppointmentStatus.COMPLETED).get(0));
    }

    @Test
    public void testGetAppointmentByClient(){
        assertEquals(0, appointmentService.getData().size());
        Appointment appointment1 = appointmentService.add(
            cosmeticTreatment1,
            cosmetologist1, 
            client1, 
            LocalDate.now(),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        Appointment appointment2 = appointmentService.add(
            cosmeticTreatment2,
            cosmetologist2, 
            client1, 
            LocalDate.now(),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        assertEquals(2, appointmentService.getData().size());
        assertEquals(2, appointmentService.getByClient(client1).size());
        assertEquals(appointment1, appointmentService.getByClient(client1).get(0));
        assertEquals(appointment2, appointmentService.getByClient(client1).get(1));
    }

    @Test
    public void testGetAppointmentByClientAndStatus(){
        assertEquals(0, appointmentService.getData().size());
        Appointment appointment1 = appointmentService.add(
            cosmeticTreatment1,
            cosmetologist1, 
            client1, 
            LocalDate.now(),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.COMPLETED
        );
        appointmentService.add(
            cosmeticTreatment2,
            cosmetologist2, 
            client1, 
            LocalDate.now(),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        assertEquals(2, appointmentService.getData().size());
        assertEquals(1, appointmentService.getByClientAndStatus(client1, AppointmentStatus.COMPLETED).size());
        assertEquals(appointment1, appointmentService.getByClientAndStatus(client1, AppointmentStatus.COMPLETED).get(0));
    }

    @Test
    public void testGetAppointmentByDateAndCosmetologist(){
        assertEquals(0, appointmentService.getData().size());
        Appointment appointment1 = appointmentService.add(
            cosmeticTreatment1,
            cosmetologist1, 
            client1, 
            LocalDate.of(2023, 1, 1),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.COMPLETED
        );
        Appointment appointment2 = appointmentService.add(
            cosmeticTreatment2,
            cosmetologist1, 
            client2, 
            LocalDate.of(2023, 1, 1),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        assertEquals(2, appointmentService.getData().size());
        assertEquals(2, appointmentService.getByDateAndCosmetologist(LocalDate.of(2023, 1, 1), cosmetologist1).size());
        assertEquals(appointment1, appointmentService.getByDateAndCosmetologist(LocalDate.of(2023, 1, 1), cosmetologist1).get(0));
        assertEquals(appointment2, appointmentService.getByDateAndCosmetologist(LocalDate.of(2023, 1, 1), cosmetologist1).get(1));
    }

    @Test
    public void testGetAppointmentByStatus(){
        assertEquals(0, appointmentService.getData().size());
        Appointment appointment1 = appointmentService.add(
            cosmeticTreatment1,
            cosmetologist1, 
            client1, 
            LocalDate.of(2023, 1, 1),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.COMPLETED
        );
        appointmentService.add(
            cosmeticTreatment2,
            cosmetologist1, 
            client2, 
            LocalDate.of(2023, 1, 1),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        assertEquals(2, appointmentService.getData().size());
        assertEquals(1, appointmentService.getByStatus(AppointmentStatus.COMPLETED).size());
        assertEquals(appointment1, appointmentService.getByStatus(AppointmentStatus.COMPLETED).get(0));
    }

    @Test
    public void testGetAppointmentByCosmetologistForTimePeriod(){
        assertEquals(0, appointmentService.getData().size());
        appointmentService.add(
            cosmeticTreatment1,
            cosmetologist1, 
            client1, 
            LocalDate.of(2023, 1, 1),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.COMPLETED
        );
        appointmentService.add(
            cosmeticTreatment2,
            cosmetologist1, 
            client2, 
            LocalDate.of(2023, 1, 1),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.COMPLETED
        );
        appointmentService.add(
            cosmeticTreatment2,
            cosmetologist1, 
            client2, 
            LocalDate.of(2023, 2, 1),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.COMPLETED
        );
        assertEquals(3, appointmentService.getData().size());
        assertEquals(2, appointmentService.getByCosmetologistForTimePeriod(cosmetologist1, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 1)).size());
        assertEquals(1, appointmentService.getByCosmetologistForTimePeriod(cosmetologist1, LocalDate.of(2023, 1, 12), LocalDate.of(2023, 3, 1)).size());
    }

    @Test
    public void testGetAppointmentByStatusForTimePeriod(){
        assertEquals(0, appointmentService.getData().size());
        appointmentService.add(
            cosmeticTreatment1,
            cosmetologist1, 
            client1, 
            LocalDate.of(2023, 1, 1),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.COMPLETED
        );
        appointmentService.add(
            cosmeticTreatment2,
            cosmetologist1, 
            client2, 
            LocalDate.of(2023, 1, 1),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        appointmentService.add(
            cosmeticTreatment2,
            cosmetologist1, 
            client2, 
            LocalDate.of(2023, 1, 2),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        assertEquals(3, appointmentService.getData().size());
        assertEquals(1, appointmentService.getByStatusForTimePeriod(AppointmentStatus.COMPLETED, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 1)).size());
        assertEquals(2, appointmentService.getByStatusForTimePeriod(AppointmentStatus.SCHEDULED, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 2)).size());
    }

    @Test
    public void testGetTotalIncomeForTimePeriod(){
        assertEquals(0, appointmentService.getData().size());
        appointmentService.add(
            cosmeticTreatment1,
            cosmetologist1, 
            client1, 
            LocalDate.of(2023, 1, 1),
            LocalTime.of(12, 0),
            1000,
            AppointmentStatus.COMPLETED
        );
        appointmentService.add(
            cosmeticTreatment2,
            cosmetologist1, 
            client2, 
            LocalDate.of(2023, 1, 1),
            LocalTime.of(12, 0),
            2000,
            AppointmentStatus.COMPLETED
        );
        appointmentService.add(
            cosmeticTreatment2,
            cosmetologist1, 
            client2, 
            LocalDate.of(2023, 1, 2),
            LocalTime.of(12, 0),
            3000,
            AppointmentStatus.SCHEDULED
        );
        assertEquals(3, appointmentService.getData().size());
        assertEquals(3000, appointmentService.getTotalIncomeForTimePeriod(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 1)));
        assertEquals(6000, appointmentService.getTotalIncomeForTimePeriod(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 2)));
    }
}
