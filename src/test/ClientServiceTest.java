package test;

import org.junit.Test;

import entity.Client;
import entity.Gender;
import service.ClientService;
import service.ServiceRegistry;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;

public class ClientServiceTest {
    private static ClientService clientService;

    @BeforeClass
    public static void setUp() {
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();
        serviceRegistry.initializeServices();
        clientService = serviceRegistry.getClientService();
    }

    @Before
    public void reset() {
        clientService.getData().clear();
    }

    @Test
    public void testAddClient() {
        assertEquals(0, clientService.getData().size());
        Client client = clientService.add("username",
            "password", 
            "name", 
            "lastname", 
            Gender.MALE, 
            "066666666", 
            "adress"
        );
        assertEquals(1, clientService.getData().size());
        assertEquals(client, clientService.getData().get(0));
    }

    @Test
    public void testRemoveClient(){
        assertEquals(0, clientService.getData().size());
        Client client = clientService.add(
            "username",
            "password", 
            "name", 
            "lastname", 
            Gender.MALE, 
            "066666666", 
            "adress"
        );
        assertEquals(1, clientService.getData().size());
        clientService.remove(client);
        assertEquals(0, clientService.getData().size());
        assertFalse(clientService.getData().contains(client));
    }

    @Test
    public void testGetClientByUsername(){
        Client client0 = clientService.add(
            "client0",
            "password", 
            "name", 
            "lastname", 
            Gender.MALE, 
            "066666666", 
            "adress"
        );
        Client client1 = clientService.add(
            "client1",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress"
        );
        assertEquals(client0, clientService.getByUsername("client0"));
        assertEquals(client1, clientService.getByUsername("client1"));
    }

    @Test
    public void testGetClientsWithLoyaltyCard(){
        clientService.add(
            "client0",
            "password", 
            "name", 
            "lastname", 
            Gender.MALE, 
            "066666666", 
            "adress"
        );
        Client client1 = clientService.add(
            "client1",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress"
        );
        client1.setHasLoyaltyCard(true);
        assertEquals(1, clientService.getByLoyaltyCardStatus(true).size());
        assertEquals(client1, clientService.getByLoyaltyCardStatus(true).get(0));
    }

    @Test
    public void testGetClientsWithoutLoyaltyCard(){
        Client client0 = clientService.add(
            "client0",
            "password", 
            "name", 
            "lastname", 
            Gender.MALE, 
            "066666666", 
            "adress"
        );
        Client client1 = clientService.add(
            "client1",
            "password",
            "name",
            "lastname",
            Gender.MALE,
            "066666666",
            "adress"
        );
        client1.setHasLoyaltyCard(true);
        assertEquals(1, clientService.getByLoyaltyCardStatus(false).size());
        assertEquals(client0, clientService.getByLoyaltyCardStatus(false).get(0));
    } 
}
