package sg.edu.ntu.nutrimate.entity;

public class CustomerEntityStatus {

    private boolean userIDExist;
    private boolean emailExist;

     public boolean isUserIDExist() {
        return userIDExist;
    }
    public void setUserIDExist(boolean userIDExist) {
        this.userIDExist = userIDExist;
    }
    public boolean isEmailExist() {
        return emailExist;
    }
    public void setEmailExist(boolean emailExist) {
        this.emailExist = emailExist;
    }
    
}
