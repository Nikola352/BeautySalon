package ui.manager.model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import entity.Client;
import entity.Gender;

public class ClientTableModel extends AbstractTableModel {
    private ArrayList<Client> clients;
    private String[] columnNames = {"Ime", "Prezime", "Korisničko ime", "Lozinka", "Pol", "Telefon", "Adresa", "Potrošio", "Ima karticu lojalnosti"};

    public ClientTableModel(ArrayList<Client> clients) {
        this.clients = clients;
    }

    @Override
    public int getRowCount() {
        return clients.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        if(clients.isEmpty() || row >= clients.size())
            return null;
        Client client = clients.get(row);
        switch (column) {
            case 0:
                return client.getName();
            case 1:
                return client.getLastname();
            case 2:
                return client.getUsername();
            case 3:
                return client.getPassword();
            case 4:
                if(client.getGender() == Gender.MALE)
                    return "Muški";
                else if(client.getGender() == Gender.FEMALE)
                    return "Ženski";
                else 
                    return null;
            case 5:
                return client.getPhoneNum();
            case 6:
                return client.getAddress();
            case 7:
                return client.getTotalSpent();
            case 8:
                return client.getHasLoyaltyCard();
            default:
                return null;
        }
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
        fireTableDataChanged();
    }

    public Client getClient(int row) {
        return clients.get(row);
    }

    public void addClient(Client client) {
        clients.add(client);
        fireTableDataChanged();
    }

    public void removeClient(int row) {
        clients.remove(row);
        fireTableDataChanged();
    }
}
