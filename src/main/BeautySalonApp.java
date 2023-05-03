package main;

import entity.User;
import service.ServiceRegistry;
import ui.LoginCLI;

public class BeautySalonApp {
    private User currentUser;
    ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();

    public BeautySalonApp() {
        serviceRegistry.loadData();
    }

    public void login(){
        LoginCLI loginCLI = new LoginCLI();
        currentUser = loginCLI.run();
    }

    public void logout(){
        currentUser = null;
    }

    public void exit(){
        System.out.println("Bye!");
        serviceRegistry.saveData();
    }

    public void run() {
        while(true){
            login();
            if (currentUser == null)
                break;
            else if(!currentUser.showCLI())
                break;
        }
        exit();
    }

	public static void main(String[] args) {
        BeautySalonApp app = new BeautySalonApp();
        app.run();
	}

}
