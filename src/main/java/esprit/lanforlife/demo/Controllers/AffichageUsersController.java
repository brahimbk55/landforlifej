package esprit.lanforlife.demo.Controllers;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


import esprit.lanforlife.demo.Entities.User;
import esprit.lanforlife.demo.Service.UserService;
import esprit.lanforlife.demo.Utils.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.stage.Stage;


public class AffichageUsersController {

    @FXML
    private VBox VBoxShowUsers;

    @FXML
    private TableColumn<User, String> nomCol;

    @FXML
    private TableColumn<User, String> prenomCol;

    @FXML
    private TableColumn<User, String> emailCol;

    @FXML
    private TableColumn<User, String> roleCol;

    @FXML
    private TableColumn<User, Boolean> isVerifiedCol;
    @FXML
    private TableColumn<User, Date> dateCol;

    @FXML
    private TableView<User> tableViewUsers;


    @FXML
    private TextField tfSearch;

    @FXML
    private ComboBox comboBox;

    ObservableList<User> userList = FXCollections.observableArrayList();

    Button blockButton = null;


    @FXML
    private void initialize() {
        fnReloadData();

    }

    private void fnReloadData() {
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date_naissance"));
        isVerifiedCol.setCellValueFactory(new PropertyValueFactory<>("verified"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        tableViewUsers.getItems().clear();
        tableViewUsers.getItems().addAll(loadDataFromDatabase());
        ObservableList<String> listTrier = FXCollections.observableArrayList("nom", "email", "date_naissance", "prenom","role");
        comboBox.setItems(listTrier);


        FilteredList<User> filtredData = new FilteredList<>(userList, e -> true);
        tfSearch.setOnKeyReleased(e -> {
            tfSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filtredData.setPredicate((Predicate<? super User>) user -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFiler = newValue.toLowerCase();
                    if (user.getNom().contains(lowerCaseFiler)) {
                        return true;
                    } else if (user.getPrenom().toLowerCase().contains(lowerCaseFiler)) {
                        return true;
                    } else if (user.getEmail().toLowerCase().contains(lowerCaseFiler)) {
                        return true;
                    } else if (user.getDate_naissance() != null && user.getDate_naissance().toString().toLowerCase().contains(lowerCaseFiler)) {
                        return true;

                    } else {
                        // Modification ici pour la comparaison d'un attribut booléen
                        if (Boolean.toString(user.getIs_verified()).contains(lowerCaseFiler)) {
                            return true;
                        }
                    }

                    return false;
                });
            });
            SortedList<User> sortedData = new SortedList<>(filtredData);
            sortedData.comparatorProperty().bind(tableViewUsers.comparatorProperty());
            tableViewUsers.setItems(sortedData);
        });

    }

    private List<User> loadDataFromDatabase() {
        List<User> data = new ArrayList<>();
        UserService us = new UserService();
        for (int i = 0; i < us.getAll().size(); i++) {
            System.out.println(us.getAll().get(i).toString());
            User user = us.getAll().get(i);
            if (user.getIs_verified())
                user.setVerified("Verified");
            else
                user.setVerified("Not Verified");
            if (user.getRoles().equals("[\"ROLE_ADMIN\"]"))
                user.setRole("ADMIN");
            else
                user.setRole("USER");
            data.add(user);



        }
        return data;
    }

    @FXML
    void Select(ActionEvent event) {
        if (comboBox.getSelectionModel().getSelectedItem().toString().equals("prenom")) {
            userList.clear();
            try (Connection connection = ConnectionManager.getConnection()) {
                String query = "SELECT * FROM `user` WHERE id != 1 ORDER BY prenom ASC";
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        // Récupération de l'état de vérification de l'utilisateur
                        String verificationStatus = resultSet.getInt("is_verified") == 1 ? "Compte Verified" : "Compte non Verified";

                        User user = new User();
                        user.setAddress(resultSet.getString("address"));
                        user.setEmail(resultSet.getString("email"));
                        user.setNom(resultSet.getString("nom"));
                        user.setPrenom(resultSet.getString("prenom"));
                        user.setRoles(resultSet.getString("roles"));
                        user.setDate_naissance(resultSet.getDate("date_naissance"));
                        user.setPassword(resultSet.getString("password"));
                        user.setIs_verified(resultSet.getBoolean("is_verified"));
                        if (user.getIs_verified())
                            user.setVerified("Verified");
                        else
                            user.setVerified("Not Verified");
                        if (user.getRoles().equals("[\"ROLE_ADMIN\"]"))
                            user.setRole("ADMIN");
                        else
                            user.setRole("USER");
                        UserService us = new UserService();
                        userList.add(user);
                    }
                }
                tableViewUsers.setItems(userList);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        }else if (comboBox.getSelectionModel().getSelectedItem().toString().equals("role")) {
            userList.clear();
            try (Connection connection = ConnectionManager.getConnection()) {
                String query = "SELECT * FROM `user` WHERE id != 1 ORDER BY roles ASC";
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        // Récupération de l'état de vérification de l'utilisateur
                        String verificationStatus = resultSet.getInt("is_verified") == 1 ? "Compte Verified" : "Compte non Verified";

                        User user = new User();
                        user.setAddress(resultSet.getString("address"));
                        user.setEmail(resultSet.getString("email"));
                        user.setNom(resultSet.getString("nom"));
                        user.setPrenom(resultSet.getString("prenom"));
                        user.setRoles(resultSet.getString("roles"));
                        user.setDate_naissance(resultSet.getDate("date_naissance"));
                        user.setPassword(resultSet.getString("password"));
                        user.setIs_verified(resultSet.getBoolean("is_verified"));
                        if (user.getIs_verified())
                            user.setVerified("Verified");
                        else
                            user.setVerified("Not Verified");
                        if (user.getRoles().equals("[\"ROLE_ADMIN\"]"))
                            user.setRole("ADMIN");
                        else
                            user.setRole("USER");
                        UserService us = new UserService();
                        userList.add(user);
                    }
                }
                tableViewUsers.setItems(userList);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        } else if (comboBox.getSelectionModel().getSelectedItem().toString().equals("email")) {
            userList.clear();
            try (Connection connection = ConnectionManager.getConnection()) {
                String query = "SELECT * FROM `user` WHERE id != 1 ORDER BY email ASC";
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        // Récupération de l'état de vérification de l'utilisateur
                        String verificationStatus = resultSet.getInt("is_verified") == 1 ? "Compte Verified" : "Compte non Verified";
                        User user = new User();
                        user.setAddress(resultSet.getString("address"));
                        user.setEmail(resultSet.getString("email"));
                        user.setNom(resultSet.getString("nom"));
                        user.setPrenom(resultSet.getString("prenom"));
                        user.setRoles(resultSet.getString("roles"));
                        user.setDate_naissance(resultSet.getDate("date_naissance"));
                        user.setPassword(resultSet.getString("password"));
                        user.setIs_verified(resultSet.getBoolean("is_verified"));
                        if (user.getIs_verified())
                            user.setVerified("Verified");
                        else
                            user.setVerified("Not Verified");
                        if (user.getRoles().equals("[\"ROLE_ADMIN\"]"))
                            user.setRole("ADMIN");
                        else
                            user.setRole("USER");
                        UserService us = new UserService();
                        userList.add(user);
                    }
                }
                tableViewUsers.setItems(userList);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } else if (comboBox.getSelectionModel().getSelectedItem().toString().equals("nom")) {
            userList.clear();
            try (Connection connection = ConnectionManager.getConnection()) {
                String query = "SELECT * FROM `user` WHERE id != 1 ORDER BY nom ASC";
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        // Récupération de l'état de vérification de l'utilisateur
                        String verificationStatus = resultSet.getInt("is_verified") == 1 ? "Compte Verified" : "Compte non Verified";

                        User user = new User();
                        user.setAddress(resultSet.getString("address"));
                        user.setEmail(resultSet.getString("email"));
                        user.setNom(resultSet.getString("nom"));
                        user.setPrenom(resultSet.getString("prenom"));
                        user.setRoles(resultSet.getString("roles"));
                        user.setDate_naissance(resultSet.getDate("date_naissance"));
                        user.setPassword(resultSet.getString("password"));
                        user.setIs_verified(resultSet.getBoolean("is_verified"));
                        if (user.getIs_verified())
                            user.setVerified("Verified");
                        else
                            user.setVerified("Not Verified");
                        if (user.getRoles().equals("[\"ROLE_ADMIN\"]"))
                            user.setRole("ADMIN");
                        else
                            user.setRole("USER");
                        UserService us = new UserService();
                        userList.add(user);
                    }
                }
                tableViewUsers.setItems(userList);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }


        } else if (comboBox.getSelectionModel().getSelectedItem().toString().equals("date_naissance")) {
            userList.clear();
            try (Connection connection = ConnectionManager.getConnection()) {
                String query = "SELECT * FROM `user` WHERE id != 1 ORDER BY date_naissance ASC";
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        // Récupération de l'état de vérification de l'utilisateur
                        String verificationStatus = resultSet.getInt("is_verified") == 1 ? "Compte Verified" : "Compte non Verified";

                        User user = new User();
                        user.setAddress(resultSet.getString("address"));
                        user.setEmail(resultSet.getString("email"));
                        user.setNom(resultSet.getString("nom"));
                        user.setPrenom(resultSet.getString("prenom"));
                        user.setRoles(resultSet.getString("roles"));
                        user.setDate_naissance(resultSet.getDate("date_naissance"));
                        user.setPassword(resultSet.getString("password"));
                        user.setIs_verified(resultSet.getBoolean("is_verified"));
                        if (user.getIs_verified())
                            user.setVerified("Verified");
                        else
                            user.setVerified("Not Verified");
                        if (user.getRoles().equals("[\"ROLE_ADMIN\"]"))
                            user.setRole("ADMIN");
                        else
                            user.setRole("USER");
                        UserService us = new UserService();

                        userList.add(user);
                    }
                }
                tableViewUsers.setItems(userList);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @FXML
    void handleChart(ActionEvent event) throws IOException {
// Load the FXML file for the new stage
        Parent root = FXMLLoader.load(getClass().getResource("/esprit/lanforlife/demo/Chart.fxml"));
        // Create the new stage
        Stage newStage = new Stage();
        // Set the title of the new stage
        newStage.setTitle("BarChart");
        // Create the scene for the new stage
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        // Show the new stage
        newStage.show();

    }
}