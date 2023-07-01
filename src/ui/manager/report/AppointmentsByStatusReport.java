package ui.manager.report;

import java.time.LocalDate;

import javax.swing.JFrame;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;

import entity.AppointmentStatus;
import service.AppointmentService;
import service.ServiceRegistry;

public class AppointmentsByStatusReport extends JFrame {

    private PieChart chart;

    AppointmentService appointmentService;

    public AppointmentsByStatusReport() {
        setTitle("Izvještaj o broju termina po statusu");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        appointmentService = serviceRegistry.getAppointmentService();

        initializeChart();
        add(new XChartPanel<PieChart>(chart));
    }

    private void initializeChart() {
        chart = new PieChartBuilder().width(700).height(600).title("Broj tretmana po statusu").build();
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysAgo = now.minusDays(30);
        chart.addSeries("Zakazani", appointmentService.getNumByStatusForTimePeriod(AppointmentStatus.SCHEDULED, thirtyDaysAgo, now));
        chart.addSeries("Završeni", appointmentService.getNumByStatusForTimePeriod(AppointmentStatus.COMPLETED, thirtyDaysAgo, now));
        chart.addSeries("Otkazao salon", appointmentService.getNumByStatusForTimePeriod(AppointmentStatus.CANCELED_BY_SALON, thirtyDaysAgo, now));
        chart.addSeries("Otkazao klijent", appointmentService.getNumByStatusForTimePeriod(AppointmentStatus.CANCELED_BY_CLIENT, thirtyDaysAgo, now));
        chart.addSeries("Nije se pojavio", appointmentService.getNumByStatusForTimePeriod(AppointmentStatus.NOT_SHOWED_UP, thirtyDaysAgo, now));
    }
    
}
