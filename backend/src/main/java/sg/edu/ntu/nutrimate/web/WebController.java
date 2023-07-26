package sg.edu.ntu.nutrimate.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.ntu.nutrimate.security.UserSecurityServiceImpl;

@CrossOrigin(origins={"http://localhost:3000/"}, allowCredentials = "true")
@Controller
public class WebController {

    @Autowired
    UserSecurityServiceImpl userSecurityService;

    // @RequestMapping(value = "/nutrimateapp")
    // public String forward(){
    //     return "forward:/";
    // }

    @RequestMapping(value = "/index")
    public String index(Model model) {
        String userDisplayName = userSecurityService.getAuthenticatedUserName();
        model.addAttribute("userGreeting", "Hello, " + userDisplayName);   
        return "index";
    }
    
    @RequestMapping(value = "/signin")
    public String signin(@RequestParam (required = false) String logout, Model model) {
        if(logout != null && logout.isBlank()){
            logout = "true";
            model.addAttribute("logout", logout);
        }        
        return "signin";   
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "signin";
    }

    @RequestMapping(value = "/logout")
    public String signout(Model model) {
        model.addAttribute("logout", true);   
        return "signin";
    }

    @RequestMapping(value = "/signin-error.html")
    public String signinError(HttpServletRequest request, Model model) {

        String errorMessage = request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION).toString();
        model.addAttribute("loginError", errorMessage);   

        return "signin";
    }

//     // @GetMapping("/login-error")
//     // public String login(HttpServletRequest request, Model model) {
//     //     HttpSession session = request.getSession(false);
//     //     String errorMessage = null;
//     //     if (session != null) {
//     //         AuthenticationException ex = (AuthenticationException) session
//     //                 .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
//     //         if (ex != null) {
//     //             errorMessage = ex.getMessage();
//     //         }
//     //     }
//     //     model.addAttribute("error", errorMessage);
//     //     return "login";
//     // }
    
}
