package ui.manager.report;

import java.time.LocalDate;

import javax.swing.JFrame;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.XChartPanel;

import entity.Cosmetologist;
import service.AppointmentService;
import service.CosmetologistService;
import service.ServiceRegistry;

public class CosmetologistReport extends JFrame {

    private PieChart chart;

    private AppointmentService appointmentService;
    private CosmetologistService cosmetologistService;

    public CosmetologistReport() {
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        appointmentService = serviceRegistry.getAppointmentService();
        cosmetologistService = serviceRegistry.getCosmetologistService();

        setTitle("Izvještaj o broju termina po kozmetičaru");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeChart();
        add(new XChartPanel<PieChart>(chart));
    }
    
    private void initializeChart() {
        chart = new PieChart(700, 600);
        chart.setTitle("Broj tretmana po kozmetičaru");
        for (Cosmetologist cosmetologist : cosmetologistService.getData()) {
            LocalDate now = LocalDate.now();
            LocalDate thirtyDaysAgo = now.minusDays(30);
            chart.addSeries(cosmetologist.toString(), appointmentService.getNumByCosmetologistForTimePeriod(cosmetologist, thirtyDaysAgo, now));
        }
    }
}
