package service;

public class ServiceRegistry {
    private static ServiceRegistry instance;

    private PriceListService priceListService;
    private TreatmentTypeService treatmentTypeService;
    private CosmeticTreatmentService cosmeticTreatmentService;
    private ClientService clientService;
    private CosmetologistService cosmetologistService;
    private ReceptionistService receptionistService;
    private ManagerService managerService;
    private BeautySalonService beautySalonService;
    private AppointmentService appointmentService;

    private ServiceRegistry() {}

    public static ServiceRegistry getInstance() {
        if(instance == null) {
            instance = new ServiceRegistry();
        }
        return instance;
    }

    public BeautySalonService getBeautySalonService() {
        return beautySalonService;
    }

    public TreatmentTypeService getTreatmentTypeService() {
        return treatmentTypeService;
    }

    public CosmeticTreatmentService getCosmeticTreatmentService() {
        return cosmeticTreatmentService;
    }

    public PriceListService getPriceListService() {
        return priceListService;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public CosmetologistService getCosmetologistService() {
        return cosmetologistService;
    }

    public ReceptionistService getReceptionistService() {
        return receptionistService;
    }

    public ManagerService getManagerService() {
        return managerService;
    }

    public AppointmentService getAppointmentService() {
        return appointmentService;
    }

    public void initializeServices(){
        if(priceListService == null)
        priceListService = new PriceListService();
        if(treatmentTypeService == null)
            treatmentTypeService = new TreatmentTypeService();
        if(cosmeticTreatmentService == null)
            cosmeticTreatmentService = new CosmeticTreatmentService();
        if(clientService == null)
            clientService = new ClientService();
        if(cosmetologistService == null)
            cosmetologistService = new CosmetologistService();
        if(receptionistService == null)
            receptionistService = new ReceptionistService();
        if(managerService == null)
            managerService = new ManagerService();
        if(beautySalonService == null)
            beautySalonService = new BeautySalonService();
        if(appointmentService == null)
            appointmentService = new AppointmentService();
    }

    public void loadData(){
        initializeServices();
        treatmentTypeService.loadData();
        cosmeticTreatmentService.loadData();
        priceListService.loadPriceList();
        clientService.loadData();
        cosmetologistService.loadData();
        receptionistService.loadData();
        managerService.loadData();
        beautySalonService.loadBeautySalon();
        appointmentService.loadData();
    }

    public void saveData(){
        priceListService.savePriceList();
        treatmentTypeService.saveData();
        cosmeticTreatmentService.saveData();
        clientService.saveData();
        cosmetologistService.saveData();
        receptionistService.saveData();
        managerService.saveData();
        beautySalonService.saveBeautySalon();
        appointmentService.saveData();
    }
}
