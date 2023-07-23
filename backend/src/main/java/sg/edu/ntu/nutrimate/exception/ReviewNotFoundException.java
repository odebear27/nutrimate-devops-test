package sg.edu.ntu.nutrimate.exception;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(int id){
        super("Review is not found: "+ id);
    }
}
