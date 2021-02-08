package tikape.tasks.domain;

public class Task {

    private Integer id;
    private String name;

    public Task(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
