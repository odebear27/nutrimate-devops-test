package sg.edu.ntu.nutrimate.exception;

public class RecipeNotFoundException extends RuntimeException {

    public RecipeNotFoundException() {
        super("No recipes found.");
    }
}
