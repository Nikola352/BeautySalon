package ui;

import entity.Receptionist;

public class ReceptionistCLI implements CLI {
    private Receptionist user;

    public ReceptionistCLI(Receptionist user) {
        this.user = user;
    }

    @Override
    public boolean run() {
        System.out.println("Receptionist CLI");
        System.out.println("Welcome " + user.getUsername());
        return true;
    }
}
