interface TaskTracker {
    fun addTask(task: String)
    fun completeTask(task: String)
    fun getTasks(): List<String>
    fun getCompletedTasks(): List<String>
}

class SimpleTaskTracker : TaskTracker {
    private val tasks = mutableListOf<String>()
    private val completedTasks = mutableListOf<String>()

    override fun addTask(task: String) {
        tasks.add(task)
    }

    override fun completeTask(task: String) {
        if (tasks.remove(task)) {
            completedTasks.add(task)
        } else {
            println("Task not found.")
        }
    }

    override fun getTasks(): List<String> = tasks
    override fun getCompletedTasks(): List<String> = completedTasks
}
// fix the error here
class Project(taskTracker: TaskTracker): TaskTracker by taskTracker