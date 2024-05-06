package esprit.lanforlife.demo.Service;

import esprit.lanforlife.demo.Entities.ResetPassword;
import esprit.lanforlife.demo.Entities.User;
import esprit.lanforlife.demo.Interfaces.IResetPasswordService;
import esprit.lanforlife.demo.Utils.ConnectionManager;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResetPasswordService implements IResetPasswordService {


    @Override
    public void create(ResetPassword entity) {

        try (Connection connection = ConnectionManager.getConnection();)
        {

            User user = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;


            String query = "SELECT * FROM user WHERE email = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, entity.getUser().getEmail());

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setNom(resultSet.getString("nom"));
                user.setRoles(resultSet.getString("roles"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
                user.setPrenom(resultSet.getString("prenom"));
                user.setIs_verified(resultSet.getBoolean("is_verified"));
                user.setAddress(resultSet.getString("address"));
                user.setDate_naissance(resultSet.getDate("date_naissance"));

            }

            preparedStatement = connection.prepareStatement("INSERT INTO reset_password_request (user_id,selector, hashed_token, requested_at, expires_at) VALUES (?,?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");

            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, entity.getUser().getEmail());
            preparedStatement.setString(3, entity.getCode()+"");

            preparedStatement.executeUpdate();


        }catch (SQLException e) {
            System.err.println("Error creating reset password: " + e.getMessage());
        }
        }
    @Override
    public ResetPassword get(User user) {
        ResetPassword resetPassword = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reset_password_request WHERE selector = ? ORDER BY requested_at DESC LIMIT 1")) {

            preparedStatement.setString(1, user.getEmail());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                resetPassword = new ResetPassword();
                resetPassword.setUser(user);
                resetPassword.setCode(Integer.parseInt(resultSet.getString("hashed_token")));
                resetPassword.setDateCreation(resultSet.getTimestamp("requested_at"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving reset password: " + e.getMessage());
        }
        System.out.println(resetPassword.toString());
        return resetPassword;
    }

    @Override
    public void ResetPassword(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        // Implement the logic to insert a new user into the database
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE  user SET   password = ? WHERE email = ?")) {

            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, user.getEmail());

            preparedStatement.executeUpdate();
            System.out.println("password updated successfully");
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
        }
    }
}
