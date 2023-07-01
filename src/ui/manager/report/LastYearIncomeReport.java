package ui.manager.report;

import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;

import entity.TreatmentType;
import service.AppointmentService;
import service.ServiceRegistry;
import service.TreatmentTypeService;

public class LastYearIncomeReport extends JFrame {

    private CategoryChart chart;

    private AppointmentService appointmentService;
    private TreatmentTypeService treatmentTypeService;

    public LastYearIncomeReport(){
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        appointmentService = serviceRegistry.getAppointmentService();
        treatmentTypeService = serviceRegistry.getTreatmentTypeService();

        setTitle("Izvještaj o prihodu za posljednjih 12 mjeseci");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeChart();
        JPanel chartPanel = new XChartPanel<CategoryChart>(chart);
        add(chartPanel);
    }

    private void initializeChart(){
        chart = new CategoryChartBuilder().
            width(700)
            .height(600)
            .title("Prihod za 12 mjeseci")
            .xAxisTitle("mjesec")
            .yAxisTitle("prihod")
            .build();

        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "Maj", "Jun", "Jul", "Avg", "Sep", "Okt", "Nov", "Dec"};
        int currentMonth = LocalDate.now().getMonthValue();
        String[] xLabels = new String[12];
        double[] xIdx = new double[12];
        for(int i = 0; i < 12; i++){
            xLabels[i] = months[(currentMonth + i) % 12];
            xIdx[i] = (currentMonth + i) % 12 + 1;
        }
        double[] yData = appointmentService.getIncomeForLast12Months();
        chart.addSeries("Ukupan prihod", xIdx, yData);
        for(TreatmentType treatmentType : treatmentTypeService.getData()) {
            yData = appointmentService.getIncomeForLast12MonthsByCosmeticTreatment(treatmentType);
            chart.addSeries(treatmentType.getName(), xIdx, yData);
        }
    }
    
}
