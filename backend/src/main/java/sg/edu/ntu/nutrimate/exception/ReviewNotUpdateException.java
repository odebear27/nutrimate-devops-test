package sg.edu.ntu.nutrimate.exception;

public class ReviewNotUpdateException extends RuntimeException{
    public ReviewNotUpdateException(int id){
        super("Review can't update: " + id);
    }
}
