package sg.edu.ntu.nutrimate.exception;

public class CourseRegistrationNotFoundException extends RuntimeException {
    
    public CourseRegistrationNotFoundException(String userID, int courseId) {
        super("Course id " + courseId + "registration for user " + userID + " not found.");
    }

    public CourseRegistrationNotFoundException(String userID) {
        super("No course registrations found for user " + userID + ".");
    }

}
