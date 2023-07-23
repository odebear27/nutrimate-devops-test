package sg.edu.ntu.nutrimate.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PasswordDataObject {

    private String username;

    private String oldPassword;

    private  String token;

    @NotBlank(message="Password is mandatory")
    @Size(min = 8, message="Password must be at least 8 characters")
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}
