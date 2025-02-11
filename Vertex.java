import java.util.*;

public class Vertex {
    public String name;
    public HashMap<String, Integer> edges;
    public int count;
    public boolean checked;

    public Vertex(String name){
        this.name = name;
        edges = new HashMap<>();
        count = 0;
        checked = false;
    }
}
