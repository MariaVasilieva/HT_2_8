package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private final static String CREATE_SQL = "INSERT INTO CLIENT (NAME) VALUES(?)";
    private final static String GET_BY_ID_SQL = "SELECT * FROM CLIENT WHERE ID = ?";
    private final static String UPDATE_SQL = "UPDATE CLIENT SET NAME = ? WHERE ID = ?";
    private final static String DELETE_BY_ID_SQL = "DELETE FROM CLIENT WHERE ID = ?";
    private final static String GET_ALL_SQL = "SELECT * FROM CLIENT ORDER BY NAME";

    private final Connection connection;

    public ClientService() {
        try {
            connection = Database.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public long create(String name) {
        if (name == null || name.length() < 1 || name.length() > 100) {
            throw new IllegalArgumentException("Name must be between 1 and 100 characters");
        }
        try (PreparedStatement createSt = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            createSt.setString(1, name);
            createSt.executeUpdate();
            ResultSet generatedKeys = createSt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
                throw new SQLException("Failed to retrieve ID of the newly created client");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String getById(long id) {
        try (PreparedStatement getByIdSt = connection.prepareStatement(GET_BY_ID_SQL)) {
            getByIdSt.setString(1, String.valueOf(id));
            ResultSet resultSet = getByIdSt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(2);
            }
            else {
                throw new SQLException("Client with ID " + id + " not found");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setName(long id, String name) {
        if (name == null || name.length() < 1 || name.length() > 100) {
            throw new IllegalArgumentException("Name must be between 1 and 100 characters");
        }
        try (PreparedStatement updateNameSt = connection.prepareStatement(UPDATE_SQL)) {
            updateNameSt.setString(1, name);
            updateNameSt.setLong(2, id);
            int rowsUpdated = updateNameSt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Client with ID " + id + " not found");
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void deleteById(long id) {
        try (PreparedStatement deleteSt = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            deleteSt.setLong(1,id);
            int rowDeleted = deleteSt.executeUpdate();
            if (rowDeleted == 0) {
                throw new SQLException("Client with ID " + id + " not found");
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public List<Client> listAll(){
        try (PreparedStatement listAllSt = connection.prepareStatement(GET_ALL_SQL)) {
            listAllSt.executeQuery();
            ResultSet resultSet = listAllSt.getResultSet();
            List<Client> clients = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong(1);
                String name = resultSet.getString(2);
                clients.add(new Client(id, name));
            }
            return clients;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
