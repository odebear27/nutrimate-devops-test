package sg.edu.ntu.nutrimate.entity;

public class Recipe {

    private int readyInMinutes;
    private String title;
    private String summary;

    public int getReadyInMinutes() {
        return this.readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
