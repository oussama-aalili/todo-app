public class TaskManagerApp {
    public static void main(String[] args) {
        TaskService taskService = new TaskServiceImpl();

        // Create tasks
        Task task1 = new Task(0, "Buy groceries", "Milk, Bread, Eggs", false);
        Task task2 = new Task(0, "Study Java", "Complete Java Maven project", false);

        taskService.createTask(task1);
        taskService.createTask(task2);

        // Read all tasks
        System.out.println("\nAll Tasks:");
        taskService.readAllTasks().forEach(System.out::println);

        // Update a task
        task1.setCompleted(true);
        taskService.updateTask(task1);

        // Read updated task
        System.out.println("\nUpdated Task:");
        System.out.println(taskService.readTask(1));

        // Delete a task
        taskService.deleteTask(2);

        // Read all tasks after deletion
        System.out.println("\nAll Tasks After Deletion:");
        taskService.readAllTasks().forEach(System.out::println);
    }
}