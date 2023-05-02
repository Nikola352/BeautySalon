package service;

import java.io.IOException;
import java.util.ArrayList;

import entity.Client;
import entity.Gender;
import utils.CsvUtil;

public class ClientService extends Service<Client> {
    public Client add(String username, String password, String name, String lastname, Gender gender, String phoneNum, String address) {
        Client client = new Client(getNextId(), username, password, name, lastname, gender, phoneNum, address);
        add(client);
        incrementNextId();
        return client;
    }

    public Client getByUsername(String username) {
        for (Client client : getData()) {
            if (client.getUsername().equals(username)) {
                return client;
            }
        }
        return null;
    }

    @Override
    protected String getFilename() {
        return appSettings.getClientFilename();
    }

    @Override
    public void loadData(){
        try {
            ArrayList<String[]> clientStrings = CsvUtil.loadData(getFilename(), appSettings.getDelimiter());
            for (String[] clientString : clientStrings) {
                Client client = Client.parseFromCsv(clientString);
                add(client);
            }
            loadNextId();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
