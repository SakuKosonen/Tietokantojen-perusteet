
public class Todo {

    Integer id; // pääavain
    String tehtava; // tehtävän kuvaus
    Boolean tehty; // tieto onko tehtävä tehty

    public Todo(Integer id, String tehtava, Boolean tehty) {
        this.id = id;
        this.tehtava = tehtava;
        this.tehty = tehty;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTehtava() {
        return tehtava;
    }

    public void setTehtava(String tehtava) {
        this.tehtava = tehtava;
    }

    public Boolean getTehty() {
        return tehty;
    }

    public void setTehty(Boolean tehty) {
        this.tehty = tehty;
    }

}
