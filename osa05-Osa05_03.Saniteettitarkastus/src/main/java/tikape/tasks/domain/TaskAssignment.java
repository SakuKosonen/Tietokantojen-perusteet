package tikape.tasks.domain;

public class TaskAssignment {

    private Integer id;
    private Integer taskId;
    private Integer userId;
    private Boolean completed;

    public TaskAssignment(Integer id, Integer taskId, Integer userId, Boolean completed) {
        this.id = id;
        this.taskId = taskId;
        this.userId = userId;
        this.completed = completed;
    }

    public Integer getId() {
        return id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Boolean getCompleted() {
        return completed;
    }

}
