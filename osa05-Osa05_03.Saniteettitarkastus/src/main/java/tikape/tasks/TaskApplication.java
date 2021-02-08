package tikape.tasks;

import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.tasks.dao.TaskAssignmentDao;
import tikape.tasks.dao.TaskDao;
import tikape.tasks.dao.UserDao;
import tikape.tasks.database.Database;
import tikape.tasks.domain.Task;
import tikape.tasks.domain.TaskAssignment;
import tikape.tasks.domain.User;

public class TaskApplication {

    public static void main(String[] args) throws Exception {

        Database database = new Database("jdbc:sqlite:tasks.db");
        TaskDao tasks = new TaskDao(database);
        UserDao users = new UserDao(database);
        TaskAssignmentDao taskAssignments = new TaskAssignmentDao(database);

        Spark.get("/tasks", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("tasks", tasks.findAllNotAssigned());
            map.put("users", users.findAll());

            return new ModelAndView(map, "tasks");
        }, new ThymeleafTemplateEngine());

        Spark.post("/tasks", (req, res) -> {
            Task task = new Task(-1, req.queryParams("name"));
            tasks.saveOrUpdate(task);

            res.redirect("/tasks");
            return "";
        });

        // polkuun määriteltävä parametri merkitään kaksoispisteellä ja 
        // parametrin nimellä. Parametrin arvoon pääsee käsiksi kutsulla
        // req.params
        Spark.post("/tasks/:id", (req, res) -> {
            Integer taskId = Integer.parseInt(req.params(":id"));
            Integer userId = Integer.parseInt(req.queryParams("userId"));
            
            TaskAssignment ta = new TaskAssignment(-1, taskId, userId, Boolean.FALSE);
            taskAssignments.saveOrUpdate(ta);

            res.redirect("/tasks");
            return "";
        });

        Spark.get("/users", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("users", users.findAll());

            return new ModelAndView(map, "users");
        }, new ThymeleafTemplateEngine());

        Spark.post("/users", (req, res) -> {
            User user = new User(-1, req.queryParams("name"));
            users.saveOrUpdate(user);

            res.redirect("/users");
            return "";
        });
        
        Spark.get("/users/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer userId = Integer.parseInt(req.params(":id"));
            map.put("user", users.findOne(userId));
            map.put("tasks", tasks.findNonCompletedForUser(userId));

            return new ModelAndView(map, "user");
        }, new ThymeleafTemplateEngine());

    }
}
