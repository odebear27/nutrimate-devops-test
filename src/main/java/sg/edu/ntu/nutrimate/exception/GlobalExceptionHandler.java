package sg.edu.ntu.nutrimate.exception;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Handle ALL Other Exceptions
    // General exception handler - handles any uncaught exception and also not
    // expose the internal errors to the public

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ObjectError> validationErrors = ex.getBindingResult().getAllErrors();
        StringBuilder stringBuidler = new StringBuilder();

        for (ObjectError error : validationErrors) {
            stringBuidler.append(error.getDefaultMessage() + ", ");
        }

        ErrorResponse errorResponse = new ErrorResponse(stringBuidler.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("SOmething went wrong. Please try again");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // @ExceptionHandler(CustomerNotFoundException.class)
    // public ResponseEntity<Object> handleCustomerNotFoundException
    // (CustomerNotFoundException ex){
    // ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
    // return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    // }

    // @ExceptionHandler(InteractionNotFoundException.class)
    // public ResponseEntity<Object> handleInteractionNotFoundException
    // (InteractionNotFoundException ex){
    // ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
    // return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    // }
    
    @ExceptionHandler(CustomerEntityNotUniqueException.class)
    public ResponseEntity<Object> handleCustomerUserIDNotUniqueException (CustomerEntityNotUniqueException ex){
        // ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(ex.getMessage(), httpHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParameterBadRequestException.class)
    public ResponseEntity<Object> handleParameterBadRequestException (RuntimeException ex){
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleGenericBadCredentialsException(BadCredentialsException ex){
        ErrorResponse errorResponse = new ErrorResponse("Invalid username or password!");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({InvalidSecurityTokenException.class, InvalidPasswordException.class})
    public ResponseEntity<Object> handleBadCredentialsException(RuntimeException ex){
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // Alternatvie Method to handle generically all exceptions of type
    // RuntimeException
    @ExceptionHandler()
    public ResponseEntity<Object> handleResourcesNotFoundException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handler for unsuccessful deletion
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Cannot perform delete for ID does not exist");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
