package main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import entity.Appointment;
import entity.AppointmentStatus;
import entity.Client;
import entity.CosmeticTreatment;
import entity.Cosmetologist;
// import entity.Manager;
import entity.Receptionist;
import service.AppointmentService;
import service.BeautySalonService;
import service.ClientService;
import service.CosmeticTreatmentService;
import service.CosmetologistService;
// import service.ManagerService;
import service.ReceptionistService;
import service.ServiceRegistry;

public class KT3 {

    private static void printAppointments(ArrayList<Appointment> appointments) {
        System.out.println("===================================");
        for (Appointment appointment : appointments) {
            System.out.println("Zakazan tretman: ");
            System.out.println(appointment.getStatus());
            System.out.println(appointment.getCosmeticTreatment().getName());
            System.out.println(appointment.getCosmetologist().getName());
            System.out.println(appointment.getClient().getName());
            System.out.println(appointment.getDate());
            System.out.println(appointment.getTime());
            System.out.println(appointment.getPrice());
            System.out.println();
        }
        System.out.println("===================================\n\n");
    }

    private static void printSchedule(ArrayList<Integer[]> schedule) {
        System.out.println("===================================");
        for(int i = 0; i < schedule.size(); i++) {
            System.out.println("pocetak: " + schedule.get(i)[0] + ":00    trajanje: " + schedule.get(i)[1] + "min");
        }
        System.out.println("===================================\n\n");
    }

    private static void printLoyaltyCardInfo(ArrayList<Client> clients){
        System.out.println("===================================");
        for (Client client : clients) {
            System.out.println("Klijent: " + client.getName() + " " + client.getLastname());
            System.out.println("Status na kartici: " + client.getTotalSpent());
            System.out.println(client.getHasLoyaltyCard() ? "Ima karticu lojalnosti" : "Nema karticu lojalnosti");
            System.out.println();
        }
        System.out.println("===================================\n\n");
    }

    public static void main(String[] args) {
        // ServiceRegistry se koristi za upravljanje svim podacima u aplikaciji
        // podaci se inace ucitavaju iz fajla u konstruktoru main klase
        // ovdje se ucitavaju podaci potrebni za ovu kontrolnu tacku
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        serviceRegistry.loadData();
        
        // Service klase iz ServiceRegistry se koriste u aplikaciji po potrebi
        // dakle, u korisnickom interfejsu se nude one opcije kojim ulogovani korisnik ima pristup
        BeautySalonService beautySalonService = serviceRegistry.getBeautySalonService();
        // ManagerService managerService = serviceRegistry.getManagerService();
        ReceptionistService receptionistService = serviceRegistry.getReceptionistService();
        CosmetologistService cosmetologistService = serviceRegistry.getCosmetologistService();
        ClientService clientService = serviceRegistry.getClientService();
        CosmeticTreatmentService cosmeticTreatmentService = serviceRegistry.getCosmeticTreatmentService();
        AppointmentService appointmentService = serviceRegistry.getAppointmentService();

        // pribavljamo sve entitete koji se koriste u test scenariju preko id-a
        // inace se oni dobijaju preko korisnickog interfejsa (ulogovani korisnik ili kozmeticar kog je odabrao)
        // korisnici
        // Manager menadzer = managerService.getById(1);
        Receptionist recepcioner = receptionistService.getById(1);
        Cosmetologist kozmeticar1 = cosmetologistService.getById(1);
        Cosmetologist kozmeticar2 = cosmetologistService.getById(2);
        Cosmetologist kozmeticar3 = cosmetologistService.getById(3);
        Client milica = clientService.getById(1);
        Client mika = clientService.getById(2);

        // tretmani
        CosmeticTreatment relaksMasaza = cosmeticTreatmentService.getById(1);
        CosmeticTreatment sportskaMasaza = cosmeticTreatmentService.getById(2);
        CosmeticTreatment francuskiManikir = cosmeticTreatmentService.getById(3);
        CosmeticTreatment gelLak = cosmeticTreatmentService.getById(4);
        CosmeticTreatment spaManikir = cosmeticTreatmentService.getById(5);
        CosmeticTreatment spaPedikir = cosmeticTreatmentService.getById(6);
        
        // TEST SCENARIO:

        // ne postoji funkcionalna razlika izmedju online zakazivanja i zakazivanja pomocu recepcionera,
        // osimn sto se u korisnickom interfejsu klijentu omogucuje da zakaze samo za sebe, a recepcioneru za bilo kog klijenta
        // po mogucnost razmotriti opciju da se cuva ko je zakazao tretman, pa se u tom slucaju prosljedjuje ulogovani korsinik,
        // ali trenutno nema funkcionalnog razloga za to
        Appointment klijent1tretman1 = appointmentService.schedule(
            francuskiManikir,
            kozmeticar3,
            milica, 
            LocalDate.of(2023, 6, 10), 
            LocalTime.of(18, 0)
        );

        Appointment klijent1tretman2 = appointmentService.schedule(
            spaPedikir,
            kozmeticar2,
            milica, 
            LocalDate.of(2023, 6, 11), 
            LocalTime.of(9, 0)
        );

        Appointment klijent2tretman1 = appointmentService.schedule(
            sportskaMasaza,
            kozmeticar1,
            mika, 
            LocalDate.of(2023, 6, 12), 
            LocalTime.of(8, 0)
        );

        Appointment klijent2tretman2 = appointmentService.schedule(
            relaksMasaza,
            kozmeticar2,
            mika, 
            LocalDate.of(2023, 6, 13), 
            LocalTime.of(19, 0)
        );

        // prilikom zakazivanja, korisniku se nude samo termini koji su slobodni,
        // tako da nema potrebe za provjerom da li je termin slobodan
        // zato ovdje izostavljam pokusaj zakazivanja termina koji je vec zauzet iz test scenarija
        // naravno, moguce je tu provjeru vrsiti u metodi schedule() u AppointmentService,
        // ali mislim da je ovakaj nacin bolji za UX

        // svi zakazani tretmani za kozmeticara 2
        ArrayList<Appointment> kozmeticar2Appointments = appointmentService.getByCosmetologist(kozmeticar2);
        System.out.println("Zakazani tretmani kozmetricara 2:");
        printAppointments(kozmeticar2Appointments);

        // kozmeticari mogu da vide svoj raspored za odredjeni dan (bira se dan u date-pickeru, pa se prikazuje raspored za taj dan)
        // moguce je proslijediti sve tretmane (bez obzira na kozmeticara), vrsi se filtriranje u metodi getTimetable()
        ArrayList<Integer[]> kozmeticar2Schedule1 = kozmeticar2.getTimetable(kozmeticar2Appointments, LocalDate.of(2023, 6, 11));
        ArrayList<Integer[]> kozmeticar2Schedule2 = kozmeticar2.getTimetable(kozmeticar2Appointments, LocalDate.of(2023, 6, 13));
        System.out.println("Raspored kozmeticara 2 za 11.06.2023:");
        printSchedule(kozmeticar2Schedule1);
        System.out.println("Raspored kozmeticara 2 za 13.06.2023:");
        printSchedule(kozmeticar2Schedule2);

        // menadzer moze da podesi granicu za dobijanje kartice lojalnosti, i/ili da izda kartice klientima koji su dostigli tu granicu
        beautySalonService.getBeautySalon().setLoyaltyCardThreshold(3500);
        beautySalonService.updateLoyaltyCardStatus();

        // status tretmana se postavlja na COMPLETED automatski pri pokretanju aplikacije ukoliko je termin prosao
        // nakon sto je tretman prosao, menadzeri, recepcioneri i kozmeticar imaju opciju 
        // da promijene status tretmana u NOT_SHOWED_UP ili COMPLETED po potrebi
        // ukoliko termin trermana jos uvijek nije prosao i status tretmana je SCHEUDLED,
        // korisnicima se nudi opcija da otkazu tretman pozicom metode cancel(), koja vodi racuna o tome ko je otkazao tretman
        klijent1tretman1.setStatus(AppointmentStatus.COMPLETED);
        appointmentService.cancel(klijent1tretman2, milica);
        appointmentService.cancel(klijent2tretman1, recepcioner); // moze da otkaze recepcioner, kozmeticar ili menadzer
        klijent2tretman2.setStatus(AppointmentStatus.NOT_SHOWED_UP);

        System.out.println("Svi zakazani tretmani:");
        printAppointments(appointmentService.getData());

        // Napomena: ako je klijent imao dovoljno potrosenog novca u trenutku posljednje dodjele kartice lojalnosti,
        // on posjeduje karticu do sljedece promjene bez obzira na eventualne povrate novca
        System.out.println("Status na kartici lojalnosti:");
        printLoyaltyCardInfo(clientService.getData());

        // ovdje nisu uracunate plate zaposlenih, jer se one automatski azuriraju na pocetku mjeseca
        System.out.println("Ukupan prihod salona: " + beautySalonService.getBeautySalon().getTotalIncome());
        System.out.println("Ukupan rashod salona: " + beautySalonService.getBeautySalon().getTotalExpenses());
        System.out.println("Ukupan profit salona: " + beautySalonService.getBeautySalon().getTotalProfit());
        System.out.println();

        appointmentService.schedule(
            gelLak, 
            kozmeticar1, 
            milica, 
            LocalDate.of(2023, 6, 14), 
            LocalTime.of(9, 0)
        );

        // pored opcije odabira kozmeticara, nudi se i opcija da se kozmeticar odabere nasumicno
        appointmentService.schedule(
            spaManikir, 
            cosmetologistService.getRandomByTreatmentType(spaManikir.getTreatmentType()), 
            mika, 
            LocalDate.of(2023, 6, 14), 
            LocalTime.of(9, 0)
        );

        System.out.println("Zakazani tretmani za klijenta Miku Mikica:");
        printAppointments(appointmentService.getByClient(mika));

        System.out.println("Ukupan prihod salona: " + beautySalonService.getBeautySalon().getTotalIncome());
        System.out.println("Ukupan rashod salona: " + beautySalonService.getBeautySalon().getTotalExpenses());
        System.out.println("Ukupan profit salona: " + beautySalonService.getBeautySalon().getTotalProfit());
        System.out.println();

        // moguca opcija je prebaciti ove funkcionalnosti u beautySalonService, ali to zahtjeva cuvanje dodatnih podataka, 
        // tako da sam izabrao ovo zbog jednostavnosti i nedupliranja podataka
        // ovdje su u rashode uracunate plate svih zaposlenih za mjesec jun 2023
        System.out.println("Ukupnan prihod za jun 2023:");
        System.out.println(appointmentService.getTotalIncomeForTimePeriod(LocalDate.of(2023, 6, 1), LocalDate.of(2023, 6, 30)));
        System.out.println("Ukupnan rashod za jun 2023:");
        System.out.println(appointmentService.getTotalExpensesForTimePeriod(LocalDate.of(2023, 6, 1), LocalDate.of(2023, 6, 30)));
        System.out.println("Ukupnan bilans za jun 2023:");
        System.out.println(appointmentService.getTotalProfitForTimePeriod(LocalDate.of(2023, 6, 1), LocalDate.of(2023, 6, 30)));
        System.out.println();

        beautySalonService.getBeautySalon().setBonusThreshold(2000);
        beautySalonService.updateEmployeeBonus();
        ArrayList<Cosmetologist> cosmetologists = cosmetologistService.getData();
        for (Cosmetologist cosmetologist : cosmetologists) {
            System.out.println(cosmetologist.getName() + " " + cosmetologist.getLastname() + " bonus: " + cosmetologist.getBonus());
        }
    }
}
