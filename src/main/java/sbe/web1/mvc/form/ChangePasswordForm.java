package sbe.web1.mvc.form;

import org.hibernate.validator.constraints.NotEmpty;

import sbe.web1.mvc.validator.IdenticalField;

@IdenticalField(src = "newPassword1", dest = "newPassword2")
public class ChangePasswordForm {

    @NotEmpty
    String currentPassword;

    @NotEmpty
    String newPassword1;

    @NotEmpty
    String newPassword2;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword1() {
        return newPassword1;
    }

    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }
}
