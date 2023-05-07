package service;

public class ServiceRegistry {
    private static ServiceRegistry instance;

    private BeautySalonService beautySalonService;
    private PriceListService priceListService;
    private TreatmentTypeService treatmentTypeService;
    private CosmeticTreatmentService cosmeticTreatmentService;
    private ClientService clientService;
    private CosmetologistService cosmetologistService;
    private ReceptionistService receptionistService;
    private ManagerService managerService;
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

    public void loadData(){
        if(beautySalonService == null)
            beautySalonService = new BeautySalonService();
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
        if(appointmentService == null)
            appointmentService = new AppointmentService();

        beautySalonService.loadBeautySalon();
        treatmentTypeService.loadData();
        cosmeticTreatmentService.loadData();
        priceListService.loadPriceList();
        clientService.loadData();
        cosmetologistService.loadData();
        receptionistService.loadData();
        managerService.loadData();
        appointmentService.loadData();
    }

    public void saveData(){
        beautySalonService.saveBeautySalon();
        priceListService.savePriceList();
        treatmentTypeService.saveData();
        cosmeticTreatmentService.saveData();
        clientService.saveData();
        cosmetologistService.saveData();
        receptionistService.saveData();
        managerService.saveData();
        appointmentService.saveData();
    }

    public void clearData(){
        priceListService.getPriceList().getPriceList().clear();
        treatmentTypeService.getData().clear();
        cosmeticTreatmentService.getData().clear();
        clientService.getData().clear();
        cosmetologistService.getData().clear();
        receptionistService.getData().clear();
        managerService.getData().clear();
        appointmentService.getData().clear();
        this.saveData();
    }
}
