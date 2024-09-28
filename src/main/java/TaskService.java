import java.util.List;

public interface TaskService {
    void createTask(Task task);          // Create
    Task readTask(int id);               // Read
    List<Task> readAllTasks();           // Read all tasks
    void updateTask(Task task);          // Update
    void deleteTask(int id);             // Delete
}
