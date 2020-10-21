import java.util.*;
public class build {

    public static void main(String[] args){
        String[] projects = {"a", "b", "c", "d", "e", "f"};
        String[][] dependencies = {{"a", "d"}, {"f", "b"}, {"b", "d"}, {"f", "a"}, {"d", "c"} };
        build test = new build(projects, dependencies);
        test.printArr();

    }

    static Project[] arr;

    public build(String[] projects, String[][] dependencies){
        arr = this.getOrder(projects, dependencies);
    }

    public static void printArr(){
        for(int i=0; i<arr.length; i++){
            System.out.print(arr[i].getName()+" ");
        }
    }

    public Project[] getOrder(String[] projects, String[][] dependencies) {
        Graph graph = makeGraph(projects, dependencies);
        return buildOrder(graph.getNodes());
    }

    public Graph makeGraph(String[] projects, String[][] dependencies) {
        Graph graph = new Graph();

        for(int i=0; i<projects.length; i++){
            graph.getNode(projects[i]);
        }

        for(int i=0; i<dependencies.length; i++){
            graph.addEdge(dependencies[i][0], dependencies[i][1]);
        }
        return graph;
    }

    public Project[] buildOrder(ArrayList<Project> projects) {
        Project[] order = new Project[projects.size()];
        int end = noDepen(order, projects, 0);
        int counter = 0;
        while(counter < order.length) {
            Project temp = order[counter];

            if (temp == null) {
                System.out.println("Cannot be built");
                return null;
            }

            ArrayList<Project> children = temp.getChildren();
            for(int i=0; i<children.size(); i++){
                children.get(i).subDepen();
            }

            end = noDepen(order, children, end);
            counter++;
        }
        return order;
    }

    public int noDepen(Project[] order, ArrayList<Project> projects, int index) {
        for(int i=0; i<projects.size(); i++) {
            if (projects.get(i).getDependencies() == 0) {
                order[index] = projects.get(i);
                index++;
            }
        }
        return index;
    }

    class Project {
        public ArrayList<Project> children = new ArrayList<Project>();
        public HashMap<String, Project> map = new HashMap<String, Project>();
        public String name;
        public int dependencies = 0;

        public Project(String n) {	name = n;	}
        public void addNewDepen(Project node) {
            if(!map.containsKey(node.getName())) {
                children.add(node);
                node.addDepen();
            }
        }

        public void addDepen() {
            dependencies++;
        }

        public void subDepen() {
            dependencies--;
        }

        public String getName() {
            return name;
        }

        public ArrayList<Project> getChildren() {
            return children;
        }

        public int getDependencies() {
            return dependencies;
        }

    }

    class Graph {
        public ArrayList<Project> nodes = new ArrayList<Project>();
        public HashMap<String, Project> map = new HashMap<String, Project>();

        public Project getNode(String name) {
            if(!map.containsKey(name)) {
                Project node = new Project(name);
                nodes.add(node);
                map.put(name, node);
            }
            return map.get(name);
        }

        public void addEdge(String dependent, String dependency) {
            Project p1 = getNode(dependent);
            Project p2 = getNode(dependency);
            p1.addNewDepen(p2);
        }

        public ArrayList<Project> getNodes() {
            return nodes;
        }
    }

}
