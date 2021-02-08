
import java.io.PrintWriter;
import java.util.List;

public class Tallentaja {

    public void tallenna(List<? extends Object> data, String tiedosto) throws Throwable {

        PrintWriter pw = new PrintWriter(tiedosto);
        data.forEach(d -> pw.println(d.toString()));
        pw.flush();
        pw.close();
    }

}
