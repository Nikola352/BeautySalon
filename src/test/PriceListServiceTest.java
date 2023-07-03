package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.CosmeticTreatment;
import entity.TreatmentType;
import service.PriceListService;
import service.ServiceRegistry;

public class PriceListServiceTest {
    private static PriceListService priceListService;
    private static CosmeticTreatment cosmeticTreatment;

    @BeforeClass
    public static void setUp() {
        ServiceRegistry registry = ServiceRegistry.getInstance();
        registry.initializeServices();
        priceListService = registry.getPriceListService();
        TreatmentType treatmentType = new TreatmentType(0, "type", "description");
        cosmeticTreatment = new CosmeticTreatment(0, "treatment", treatmentType, 50);
    }

    @Before
    public void reset() {
        priceListService.getPriceList().getPriceList().clear();
    }

    @Test
    public void testAddPrice() {
        assertEquals(0, priceListService.getPriceList().getPriceList().size());
        priceListService.add(0, 100.0);
        assertEquals(1, priceListService.getPriceList().getPriceList().size());
        assertEquals(100.0, priceListService.getPriceList().getPriceList().get(0), 0.001);
    }

    @Test
    public void testRemovePrice() {
        priceListService.add(0, 100.0);
        assertEquals(1, priceListService.getPriceList().getPriceList().size());
        priceListService.remove(0);
        assertEquals(0, priceListService.getPriceList().getPriceList().size());
    }

    @Test
    public void getPrice(){
        priceListService.add(cosmeticTreatment.getId(), 100.0);
        assertEquals(100.0, priceListService.getPrice(cosmeticTreatment), 0.001);
    }

    @Test
    public void testGetPriceById() {
        priceListService.add(0, 100.0);
        assertEquals(100.0, priceListService.getPrice(0), 0.001);
    }

    @Test
    public void testUpdatePrice() {
        priceListService.add(0, 100.0);
        assertEquals(100.0, priceListService.getPrice(0), 0.001);
        priceListService.update(0, 200.0);
        assertEquals(200.0, priceListService.getPrice(0), 0.001);
    }
}
