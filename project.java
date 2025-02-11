import java.util.*;
import java.io.*;
public class project5 {

    public static void printHash(HashMap<Vertex, Vertex> hash){
        String str = "{";
        for (Map.Entry<Vertex,Vertex> entry: hash.entrySet()) {
            str += entry.getKey().name + " = " + entry.getValue().name + ",";
        }
        System.out.println(str.substring(0,str.length()-1) +"}");
    }

    public static int flow(HashMap<Vertex, Vertex> path, Vertex start, Vertex end){
        ArrayList<Integer> flows = new ArrayList<>();
        ArrayList<Vertex> organized = new ArrayList<>();
//        printHash(path);
        Vertex current = end;
        while(!current.name.equals(start.name)){
//            System.out.println(current.name);
            organized.add(current);
            Vertex next = path.get(current);
            flows.add(next.edges.get(current.name));
            current = next;
        }
        organized.add(start);
        int min = flows.get(0);
        for (int m: flows){
            if(m < min){
                min = m;
            }
        }
        for (int i = organized.size()-1; i>0; i--){
            Vertex crrent = organized.get(i);
            Vertex nxt = organized.get(i-1);
            crrent.edges.put(nxt.name, crrent.edges.get(nxt.name) - min);
            nxt.edges.put(crrent.name, nxt.edges.get(crrent.name) + min);
        }
        return min;
    }

    public static boolean search(HashMap<String, Vertex> graph,String strt, String end, int count, HashMap<Vertex,Vertex> path){
        path.clear();
        Vertex start = graph.get(strt);
        Queue<Vertex> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()){
            Vertex current = queue.poll();
            for(Map.Entry<String, Integer> entry: current.edges.entrySet()){

                Vertex temp = graph.get(entry.getKey());
                if(temp.count != count){
                    temp.checked = false;
                    temp.count = count;
                }
                if(entry.getValue() > 0 && !temp.checked){
                    if(temp.name.equals(end)){
                        path.put(temp, current);
                        return true;
                    }
                    path.put(temp, current);
                    queue.add(temp);
                }
            }
            current.checked = true;

        }
        return false;
    }

    public static int maxFlow(HashMap<String, Vertex> graph, Vertex strt, Vertex end){
        HashMap<Vertex, Vertex> path = new HashMap<>();
        int flow = 0;
        int count = 0;
        while(search(graph, strt.name, end.name, count, path)){
            flow += flow(path, strt, end);
            count++;
        }
        return flow;

    }


    public static void main(String[] args) {
//        Scanner input = new Scanner(System.in);
        String fpIn = args[0];
        String fpOut = args[1];
//        input.close();
        try {
            File fileIn = new File(fpIn);
            File fileOut = new File(fpOut);
            Scanner reader = new Scanner(fileIn);
            FileWriter writer = new FileWriter(fileOut);
            int numberOfCities = Integer.parseInt(reader.nextLine());
            String[] tempSoldier = reader.nextLine().split(" ");
            int[] soldierCounts = new int[6];
            for (int tmp = 0; tmp<6; tmp++){
                soldierCounts[tmp] = Integer.parseInt(tempSoldier[tmp]);
            }
            HashMap<String, Vertex> graph = new HashMap<>();
            Vertex source = new Vertex("mySource");
            Vertex end = new Vertex("KL");
            graph.put("KL", end);
            graph.put("mySource", source);
            for (int i = 0; i<6; i++){
                String[] tempLst = reader.nextLine().split(" ");
                Vertex tempVrtx = new Vertex(tempLst[0]);
                for (int j=1; j<tempLst.length; j += 2){
                    tempVrtx.edges.put(tempLst[j], Integer.parseInt(tempLst[j+1]));
                }
                source.edges.put(tempLst[0], soldierCounts[i]);
                graph.put(tempLst[0], tempVrtx);
            }
            while (reader.hasNextLine()){
                String[] tempArray = reader.nextLine().split(" ");
                Vertex tmpVertex = new Vertex(tempArray[0]);
                for (int j=1; j<tempArray.length; j += 2){
                    tmpVertex.edges.put(tempArray[j], Integer.parseInt(tempArray[j+1]));
                }
                graph.put(tempArray[0], tmpVertex);
            }
            for(Map.Entry<String, Vertex> entry: graph.entrySet()){
                for(Map.Entry<String, Integer> edge: entry.getValue().edges.entrySet()){
                    Vertex check = graph.get(edge.getKey());
                    if(!check.edges.containsKey(entry.getKey())){
                        check.edges.put(entry.getKey(), 0);
                    }
                }
            }

            writer.write(Integer.toString(maxFlow(graph, source, end)));
            writer.close();

        }
        catch (IOException e){
            System.out.println("Error");
        }
    }
}