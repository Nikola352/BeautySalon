package ui;

import entity.Cosmetologist;

public class CosmetologistCLI implements CLI {
    private Cosmetologist user;

    public CosmetologistCLI(Cosmetologist user) {
        this.user = user;
    }

    @Override
    public boolean run() {
        System.out.println("Cosmetologist CLI");
        System.out.println("Welcome " + user.getUsername());
        return true;
    }
}
