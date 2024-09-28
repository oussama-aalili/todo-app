public class Task {
    private int id;
    private String title;
    private String description;
    private boolean isCompleted;

    // Constructor
    public Task(int id, String title, String description, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    // Getters and Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return isCompleted; }

    public void setCompleted(boolean isCompleted) { this.isCompleted = isCompleted; }

    @Override
    public String toString() {
        return "Task [id=" + id + ", title=" + title + ", description=" + description + ", isCompleted=" + isCompleted + "]";
    }
}
