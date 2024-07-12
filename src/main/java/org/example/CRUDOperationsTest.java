package org.example;

import java.util.*;
import java.sql.SQLException;


public class CRUDOperationsTest {
    public static void main(String[] args) {
        try{
            ClientService clientService = new ClientService();
            System.out.println(clientService.create("Maria"));
            clientService.deleteById(1);
            clientService.setName(2,"Sergiy");
            System.out.println(clientService.getById(2));
            List<Client> clientList = clientService.listAll();
            clientList.forEach(System.out::println);
        }
        finally {
            try {
                 Database.getInstance().getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

