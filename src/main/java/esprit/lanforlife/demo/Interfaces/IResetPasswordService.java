package esprit.lanforlife.demo.Interfaces;

import esprit.lanforlife.demo.Entities.ResetPassword;
import esprit.lanforlife.demo.Entities.User;

public interface IResetPasswordService {

    public void create(ResetPassword entity);
    public ResetPassword get(User user);

    public void ResetPassword(User user);
}
