package sg.edu.ntu.nutrimate.security;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import net.bytebuddy.agent.builder.AgentBuilder.InstallationListener.ErrorSuppressing;
import sg.edu.ntu.nutrimate.logger.LogHandler;
import sg.edu.ntu.nutrimate.logger.LogHandler.Level;

// @Component("authenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    // @Autowired
    // private MessageSource messages;

    // @Autowired
    // private LocaleResolver localeResolver;

    // @Autowired
    // private HttpServletRequest request;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception) throws IOException, ServletException {
        
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        String message = exception.getMessage();

        if (loginAttemptService.isBlocked()) {
            message = "Your account is blocked as you have exceeded the number of tries.";
        }
        
        String jsonPayload = "{\"message\" : \"%s\", \"timestamp\" : \"%s\" }";
        response.getOutputStream().println(String.format(jsonPayload, message, Calendar.getInstance().getTime()));

        String errorMessage = String.format(jsonPayload, message, Calendar.getInstance().getTime());
        LogHandler.handleAuthLog(Level.ERROR, errorMessage);

        //Below two lines required to handle the error display on signin page
        super.setDefaultFailureUrl("/signin-error.html");
        super.onAuthenticationFailure(request, response, exception);
        
        request.getSession()
            .setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, message);

        // LogHandler.handleLog(Level.ERROR, "error");

        // super.onAuthenticationFailure(request, response, exception);

        // final Locale locale = localeResolver.resolveLocale(request);

        // String errorMessage = messages.getMessage("message.badCredentials", null, locale);

        // if (loginAttemptService.isBlocked()) {
        //     errorMessage = messages.getMessage("auth.message.blocked", null, locale);
        // }

        // if (exception.getMessage()
        //     .equalsIgnoreCase("Invalid User")) {
        //     errorMessage = messages.getMessage("auth.message.disabled", null, locale);
        // } 
        // else if (exception.getMessage()
        //     .equalsIgnoreCase("User is disabled")) {
        //     errorMessage = messages.getMessage("auth.message.disabled", null, locale);
        // } else if (exception.getMessage()
        //     .equalsIgnoreCase("User account has expired")) {
        //     errorMessage = messages.getMessage("auth.message.expired", null, locale);
        // } else if (exception.getMessage()
        //     .equalsIgnoreCase("blocked")) {
        //     errorMessage = messages.getMessage("auth.message.blocked", null, locale);
        // } else if (exception.getMessage()
        //     .equalsIgnoreCase("unusual location")) {
        //     errorMessage = messages.getMessage("auth.message.unusual.location", null, locale);
        // }

        // request.getSession()
        //     .setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }
}