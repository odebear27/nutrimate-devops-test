package sg.edu.ntu.nutrimate.exception;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(int id) {
        super("Course with id " + id + " not found.");
    }

    public CourseNotFoundException() {
        super("No courses found.");
    }

}
