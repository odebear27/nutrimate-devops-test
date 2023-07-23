package sg.edu.ntu.nutrimate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins={"http://localhost:3000/"}, allowCredentials = "true")
// @CrossOrigin(origins={"https://localhost:3001/"}, allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST})
// @CrossOrigin(origins={"https://localhost:3001/"}, allowCredentials = "true")
@RestController
@RequestMapping("/nutrimate")
public class AuthenticationController {

    @Autowired
    UserSecurityServiceImpl userSecurityService;

    @GetMapping("/basicauth")
    public ResponseEntity<String> basicAuthentication() {
        String userDisplayName = userSecurityService.getAuthenticatedUserName();
        return new ResponseEntity<>(userDisplayName, HttpStatus.OK); 
        // return new ResponseEntity<>("Authenticated", HttpStatus.OK);  
    }

    // @CrossOrigin(allowedHeaders = {"Authorization", "Content-Type", "Accept"})
    // @PostMapping("/logout")
    // public ResponseEntity<Object> logout() {
    //     final HttpHeaders httpHeaders= new HttpHeaders();
    //     httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    //     return new ResponseEntity<>("status: Logged Out", httpHeaders, HttpStatus.OK);
    // }

    @GetMapping("/logout")
    public void logout() {
        // return new ResponseEntity<>(HttpStatus.NO_CONTENT);  
    }

}
