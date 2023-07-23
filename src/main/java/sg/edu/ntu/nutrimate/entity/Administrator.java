package sg.edu.ntu.nutrimate.entity;

import javax.persistence.Column;

// import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="administrator")
public class Administrator {

    public Administrator(){
    }

    public Administrator(String firstName, String lastName, String email, String contactNo, String userID, String password, String userrole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contactNo = contactNo;
        this.userID = userID;
        this.password = password;
        this.userrole = userrole;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private int id;

    @Column(name="first_name", nullable = false)
    @NotBlank(message="First Name is mandatory")
    private String firstName;

    @Column(name="last_name", nullable = false)
    @NotBlank(message="Last Name is mandatory")
    private String lastName;

    @Column(name="email", unique = true, nullable = false)
    // Based on RFC 5322 standards for Email Validation
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&*+/=?`{}~^.-]+@[a-zA-Z0-9.-]+$", message="Valid Email is required")
    private String email;

    @Column(name = "contact_no")
    private String contactNo;
  
    @Column(name="userid", unique = true, nullable = false)
    @NotBlank(message="User ID is required")
    private String userID;

    @Column(name="passcode", nullable = false)
    @NotBlank(message="Password is mandatory")
    @Size(min = 8, message="Password must be at least 8 characters")
    private String password;

    @Column(name="user_role", nullable = false)   
    private String userrole;
        
    // public Customer(){
    // this.id = UUID.randomUUID().toString();
    // }

    public int getId() {
        return id;
    }    

    public void setId(int id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getContactNo() {
        return contactNo;
    }
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUserRole() {
        return userrole;
    }
    public void setUserRole(String userrole) {
        this.userrole = userrole;
    }
}
