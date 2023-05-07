package main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import entity.Appointment;
import entity.BeautySalon;
import entity.CosmeticTreatment;
import entity.Cosmetologist;
import entity.DayOfWeek;
import entity.Gender;
import entity.LevelOfEducation;
import entity.TreatmentType;
import service.AppointmentService;
import service.BeautySalonService;
import service.ClientService;
import service.CosmeticTreatmentService;
import service.CosmetologistService;
import service.ManagerService;
import service.PriceListService;
import service.ReceptionistService;
import service.ServiceRegistry;
import service.TreatmentTypeService;

public class BeautySalonApp {
    ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();

    public BeautySalonApp() {
        serviceRegistry.loadData();
    }

    // utility method for test scenario execution
    private void ispisKozmetickihTretmana(ArrayList<CosmeticTreatment> cosmeticTreatments, PriceListService priceListService){
        for(CosmeticTreatment cosmeticTreatment : cosmeticTreatments) {
            System.out.print(cosmeticTreatment.getName());
            System.out.print(" - ");
            System.out.print(cosmeticTreatment.getTreatmentType().getName());
            System.out.print(" - ");
            System.out.print(priceListService.getPrice(cosmeticTreatment));
            System.out.print(" - ");
            System.out.println(cosmeticTreatment.getDuration());
        }
    }

    // utility method for test scenario execution
    private void ispisiZakazaneTretmane(ArrayList<Appointment> appointments){
        for(Appointment appointment : appointments) {
            System.out.print(appointment.getCosmeticTreatment().getName());
            System.out.print(" - ");
            System.out.print(appointment.getCosmetologist().getName());
            System.out.print(" - ");
            System.out.print(appointment.getClient().getName());
            System.out.print(" - ");
            System.out.print(appointment.getDate());
            System.out.print(" - ");
            System.out.print(appointment.getTime());
            System.out.print(" - ");
            System.out.println(appointment.getPrice());
        }
    }

    public void run() {
        // test scenario opisan u dokumentu

        // ovaj poziv funkcije brise sve podatke iz fajlova da bi test scenario uvijek bio isti
        serviceRegistry.clearData();

        // service classes for crud operations (would normally be called from a ui class)
        BeautySalonService beautySalonService = serviceRegistry.getBeautySalonService();
        ManagerService managerService = serviceRegistry.getManagerService();
        ReceptionistService receptionistService = serviceRegistry.getReceptionistService();
        CosmetologistService cosmetologistService = serviceRegistry.getCosmetologistService();
        ClientService clientService = serviceRegistry.getClientService();
        TreatmentTypeService treatmentTypeService = serviceRegistry.getTreatmentTypeService();
        CosmeticTreatmentService cosmeticTreatmentService = serviceRegistry.getCosmeticTreatmentService();
        AppointmentService appointmentService = serviceRegistry.getAppointmentService();
        PriceListService priceListService = serviceRegistry.getPriceListService(); 

        // naziv i radno vreme salona
        BeautySalon beautySalon = beautySalonService.getBeautySalon();
        beautySalon.setName("Moj salon");
        beautySalon.setOpeningHour(8);
        beautySalon.setClosingHour(20);
        beautySalon.setWorkingDays(new ArrayList<DayOfWeek>() {{
            add(DayOfWeek.MONDAY);
            add(DayOfWeek.TUESDAY);
            add(DayOfWeek.WEDNESDAY);
            add(DayOfWeek.THURSDAY);
            add(DayOfWeek.FRIDAY);
        }});

        // menadzer
        managerService.add("nikola",
            "test123", 
            "Nikola", 
            "Nikolic", 
            Gender.MALE, 
            "064444444", 
            "Adresa", 
            LevelOfEducation.BACHELOR, 
            10, 10000, 0
        );

        // recepcioner
        receptionistService.add(
            "pera",
            "test123",
            "Pera",
            "Peric",
            Gender.MALE,
            "064444444",
            "Adresa",
            LevelOfEducation.BACHELOR,
            5,
            50000
        );
        
        // kozmeticari: dodavanje sa praznom listom tretmana za koje su obuceni (jer se tretmani dodaju kasnije)
        Cosmetologist sima = cosmetologistService.add(
            "sima",
            "test123",
            "Sima",
            "Simic",
            Gender.MALE,
            "064444444",
            "Adresa",
            LevelOfEducation.HIGH_SCHOOL,
            2,
            10000,
            0,
            new ArrayList<TreatmentType>()
        );

        cosmetologistService.add(
            "zika",
            "test123",
            "Žika",
            "Žikić",
            Gender.MALE,
            "064444444",
            "Adresa",
            LevelOfEducation.DOCTORATE,
            10,
            10000,
            0,
            new ArrayList<TreatmentType>()
        );

        cosmetologistService.add(
            "jadranka",
            "test123",
            "Jadranka",
            "Jovanovic",
            Gender.FEMALE,
            "064444444",
            "Adresa",
            LevelOfEducation.BACHELOR,
            5,
            10000,
            0,
            new ArrayList<TreatmentType>()
        );

        // klijenti
        clientService.add(
            "milica",
            "test123",
            "Milica",
            "Milic",
            Gender.FEMALE,
            "064444444",
            "Adresa"
        );

        clientService.add(
            "mika",
            "test123",
            "Mika",
            "Mikic",
            Gender.MALE,
            "064444444",
            "Adresa"
        );

        // izmena kozmeticara
        cosmetologistService.getByUsername("jadranka").setName("Jovana");
        cosmetologistService.getByUsername("jadranka").setUsername("jovana");

        // dodavanje tipova tretmana
        TreatmentType masaza = treatmentTypeService.add("masaža", "opis masaže");
        TreatmentType manikir = treatmentTypeService.add("manikir", "opis manikira");
        TreatmentType pedikir = treatmentTypeService.add("pedikir", "opis pedikira");

        // dodavanje kozmetickih tretmana
        CosmeticTreatment relaxMasaza = cosmeticTreatmentService.add(
            "Relax masaža",
            treatmentTypeService.getByName("masaža"),
            2000,
            45
        );

        cosmeticTreatmentService.add(
            "Sportska masaža",
            masaza,
            2500,
            75
        );
        
        CosmeticTreatment francuskiManikir = cosmeticTreatmentService.add(
            "Francuski manikir",
            manikir,
            1500,
            50
        );

        CosmeticTreatment gelLak = cosmeticTreatmentService.add(
            "Gel lak",
            manikir,
            1600,
            80
        );

        CosmeticTreatment spaManikir = cosmeticTreatmentService.add(
            "Spa manikir",
            manikir,
            2000,
            90
        );

        CosmeticTreatment spaPedikir = cosmeticTreatmentService.add(
            "Spa pedikir",
            masaza,
            1600,
            45
        );

        ispisKozmetickihTretmana(cosmeticTreatmentService.getData(), priceListService);

        // izmena kozmetickih tretmana
        francuskiManikir.setDuration(55);
        spaPedikir.setTreatmentType(pedikir);

        ispisKozmetickihTretmana(cosmeticTreatmentService.getData(), priceListService);

        // brisanje tipa tretmana
        treatmentTypeService.remove(pedikir);

        ispisKozmetickihTretmana(cosmeticTreatmentService.getData(), priceListService);

        // postavljanje obucenosti kozmeticara (mozemo im pristupati pomocu getByUsername, getById ili koristeci povratnu vrijednost metode add)
        sima.addTreatmentType(masaza);
        sima.addTreatmentType(manikir);
        cosmetologistService.getByUsername("jovana").addTreatmentType(manikir);

        // zakazivanje tretmana
        // nema potrebe vrsiti drugacije pretrazivanje tretmana nego po id, jer ce ui listati moguce opcije
        // ovdje se koristi povratna vrijednost metode add, ali se moze koristiti i getById
        appointmentService.add(
            relaxMasaza,
            cosmetologistService.getByUsername("sima"),
            clientService.getByUsername("milica"),
            LocalDate.of(2023, 5, 10),
            LocalTime.of(10, 0)
        );

        Appointment t2 = appointmentService.add(
            gelLak,
            cosmetologistService.getByUsername("sima"),
            clientService.getByUsername("mika"),
            LocalDate.of(2023, 5, 10),
            LocalTime.of(11, 0)
        );

        appointmentService.add(
            spaManikir,
            cosmetologistService.getByUsername("jovana"),
            clientService.getByUsername("mika"),
            LocalDate.of(2023, 5, 15),
            LocalTime.of(13, 0)
        );
        
        ispisiZakazaneTretmane(appointmentService.getData());

        // izmena zakazanog tretmana
        t2.setCosmeticTreatment(francuskiManikir);

        ispisiZakazaneTretmane(appointmentService.getData());

        // izmena cene tretmana
        priceListService.update(1, 1700);

        ispisiZakazaneTretmane(appointmentService.getData());

        // funkcije za trajno cuvanje podataka u fajlove se ne pozivaju prilikom izmjena entiteta
        // ostavljena je fleksibilnost da se to radi po potrebi (treba jos razmisliti o toj opciji)
        // u test scenariju sam ostavio poziv funkcije za cuvanje podataka u fajlove na kraju programa
        // otkomentarisati ako zelite da se podaci sacuvaju u fajlove
        serviceRegistry.saveData();
    }

	public static void main(String[] args) {
        BeautySalonApp app = new BeautySalonApp();
        app.run();
	}

}
