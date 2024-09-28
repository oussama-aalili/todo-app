import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskServiceImpl implements TaskService {
    private List<Task> tasks = new ArrayList<>();
    private int currentId = 1;

    @Override
    public void createTask(Task task) {
        task.setId(currentId++);  // Auto-incrementing ID
        tasks.add(task);
        System.out.println("Task created: " + task);
    }

    @Override
    public Task readTask(int id) {
        Optional<Task> task = tasks.stream().filter(t -> t.getId() == id).findFirst();
        return task.orElse(null);  // Return null if not found
    }

    @Override
    public List<Task> readAllTasks() {
        return tasks;
    }

    @Override
    public void updateTask(Task task) {
        Task existingTask = readTask(task.getId());
        if (existingTask != null) {
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setCompleted(task.isCompleted());
            System.out.println("Task updated: " + existingTask);
        } else {
            System.out.println("Task not found for update.");
        }
    }

    @Override
    public void deleteTask(int id) {
        Task task = readTask(id);
        if (task != null) {
            tasks.remove(task);
            System.out.println("Task deleted: " + task);
        } else {
            System.out.println("Task not found for deletion.");
        }
    }
}
