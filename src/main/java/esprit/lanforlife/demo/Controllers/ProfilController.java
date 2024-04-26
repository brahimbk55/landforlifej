package esprit.lanforlife.demo.Controllers;

import esprit.lanforlife.demo.Service.ResetPasswordService;
import esprit.lanforlife.demo.Service.UserService;
import esprit.lanforlife.demo.Entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class ProfilController implements Initializable {

    @FXML
    private VBox VBoxProfil;

    @FXML
    private Button btnModifProfil;

    @FXML
    private Button btnResetPass;

    @FXML
    private Label hello;

    @FXML
    private Label hello1;

    @FXML
    private Label hello11;

    @FXML
    private Label hello111;

    @FXML
    private Label hello1111;

    @FXML
    private Label hello11111;

    @FXML
    private Label hello1112;

    @FXML
    private Label hello2;

    @FXML
    private Label hello21;

    @FXML
    private Label lbConfirmePasswordReset;

    @FXML
    private Label lbEmailProfil;

    @FXML
    private Label lbFullAddresseProfil;

    @FXML
    private Label lbFullNameProfil;

    @FXML
    private Label lbNumTelProfil;

    @FXML
    private Label lbPasswordReset;

    @FXML
    private Pane pane;

    @FXML
    private PasswordField tfConfirmPassword;

    @FXML
    private DatePicker tfDate;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfFullName;

    @FXML
    private TextField tfNumber;

    @FXML
    private PasswordField tfPassword;


    private UserService userService;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation de fileChooser

        userService = UserService.getInstance();
        User authenticatedUser = userService.getAuthenticatedUser();
        tfEmail.setText(authenticatedUser.getEmail());
        tfDate.setValue(authenticatedUser.getDate_naissance().toLocalDate());
        tfNumber.setText(authenticatedUser.getNom());
        tfFullName.setText(authenticatedUser.getPrenom());
    }


    @FXML
    void ResetPasswordAction(ActionEvent event) throws Exception {
        userService = UserService.getInstance();
        User authenticatedUser = userService.getAuthenticatedUser();
        ResetPasswordService rps = new ResetPasswordService();
        authenticatedUser.setPassword(tfPassword.getText());
        rps.ResetPassword(authenticatedUser);
        tfPassword.setText("");
        tfConfirmPassword.setText("");
        showUpdate("Password Updated succesfully");

    }

    @FXML
    void changeProfilAction(ActionEvent event) {
        userService = UserService.getInstance();
        // Your existing initialization code...
        // Example usage:
        User authenticatedUser = userService.getAuthenticatedUser();
        authenticatedUser.setEmail(tfEmail.getText());
        authenticatedUser.setNom(tfNumber.getText());
        authenticatedUser.setPrenom(tfFullName.getText());
        authenticatedUser.setDate_naissance(Date.valueOf(tfDate.getValue()));
        userService.update(authenticatedUser);
        showUpdate("user infos updated succesfully");
    }


    private void showUpdate(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.setTitle("User Updated");
        alert.setHeaderText(null);
        alert.show();
    }
}
