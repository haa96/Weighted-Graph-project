import java.util.Scanner;
import java.io.FileReader;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Stack;

public class NetworkAnalysis {

    
    public static EdgeWeightedGraph latencyGraph; 
    public static EdgeWeightedGraph bandwidthGraph;
    public static Stack<Integer> stack = new Stack<>();
    public static int  totalVer = 0;

    public static void insert(String fileName ) {

        try {
            BufferedReader buffer = new BufferedReader(new FileReader(fileName));
            String s = buffer.readLine();

            bandwidthGraph = new EdgeWeightedGraph(Integer.parseInt(s));
            latencyGraph = new EdgeWeightedGraph(Integer.parseInt(s));

            totalVer = Integer.parseInt(s);
            s = buffer.readLine();
            while (s != null) {

                StringBuilder from = new StringBuilder();
                StringBuilder to = new StringBuilder();
                StringBuilder type = new StringBuilder();
                StringBuilder bandwidth = new StringBuilder();
                StringBuilder length = new StringBuilder();

                int i=0;
                while (s.charAt(i) != ' ')
                from.append(s.charAt(i++));
                i++;

                while (s.charAt(i) != ' ') 
                to.append(s.charAt(i++));
                i++;

                while (s.charAt(i) != ' ')
                type.append(s.charAt(i++));
                i++;

                while (s.charAt(i) != ' ') 
                bandwidth.append(s.charAt(i++));
                i++;

                while (s.charAt(i) != ' '&& i < s.length()-1)
                length.append(s.charAt(i++));
                length.append(s.charAt(s.length()-1)); 

                double speed = getSpeed(type.toString(), Double.parseDouble(length.toString()));
                bandwidthGraph.addEdge(new Edge(Integer.parseInt(from.toString()), Integer.parseInt(to.toString()), Double.parseDouble(bandwidth.toString()), type.toString()));
                latencyGraph.addEdge(new Edge(Integer.parseInt(from.toString()), Integer.parseInt(to.toString()), speed,Integer.parseInt(bandwidth.toString())));
                
                s = buffer.readLine();
            }
            buffer.close();
        } catch (IOException e) {
            System.out.println("    Error: Can't find file! ");
            System.exit(0);
        }
    }//INSERT

    private static void lowestLatency() {

        int v1,v2 =0;
        double min =  Double.POSITIVE_INFINITY;
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("  Enter vertex number (e.g. 1) ");
            System.out.print("  From vertex: ");
            v1 = scanner.nextInt();
            System.out.print("  To vertex: ");
            v2 = scanner.nextInt();
            if (v1 >= totalVer || v2 >= totalVer || v1 == v2) {
                System.out.println("    Please enter within range vertex number!");
                return;
            }
        DijkstraUndirectedSP obj1 = new DijkstraUndirectedSP(latencyGraph, v1);
        if (obj1.hasPathTo(v2)) {
           System.out.print("   Found Path from " + v1 + " to " + v2+ " : "); 
            stack.push(v2);
            for (Edge edge : obj1.pathTo(v2)) {
            if (!stack.contains(edge.v)) stack.push(edge.v);

            if (!stack.contains(edge.w)) stack.push(edge.w);
            }
            System.out.print(stack.pop());
            while (!stack.empty()) {
                System.out.print(" >> ");
                System.out.print(stack.pop());
            }
            System.out.println();
        }
        else {
            System.out.print("  Sorry! there is no path from " + v1 +  " to " + v2);
        }

        DijkstraUndirectedSP obj2 = new DijkstraUndirectedSP(latencyGraph, v1);
        if (obj2.hasPathTo(v2)) {
            for (Edge edge : obj2.pathTo(v2)) {
                if(edge.bandwidth< min){
                    min = edge.bandwidth;
                }
            }
            System.out.println("    Available Bandwidth    " + min );
        }
        System.out.println();
    
    }catch (Exception e ){
        System.out.println("    Error: Invalid input!");
            
        }
    }// LOWEST LATENCY

    private static void CopperConnected() {
        DepthFirstSearch depthFirstSearch = new DepthFirstSearch(bandwidthGraph);
        if (depthFirstSearch.copperConnected(bandwidthGraph)){
            System.out.println("    Network is copper-connected!");
        }
        else
            System.out.println("    Network is NOT copper-connected!");

    }// COPPER_CONNECTED

    private static void survive() {

        boolean isConnected = surviveFailure();
        if (!isConnected) 
            System.out.println("    Network will survive failure of any 2 vertices! ");
        else
            System.out.println("    Network will NOT survive failure of any 2 vertices!  ");
    }// REMAIN_CONNECTED

    public static boolean surviveFailure(){

        DepthFirstSearch dfs = new DepthFirstSearch(bandwidthGraph);
        return dfs.fail(bandwidthGraph);
    }// SURVIVE_FAILURE

    public static double  getSpeed(String type, double length) {
        if (type.equals("optical")){
            return (length / 200000000);
        }
        else if (type.equals("copper")){
            return  (length / 230000000);
        }
        return -1.0;
    }//GET_SPEED

    public static void main(String[]args) {
        if(args.length < 1)
        {
            System.out.println();
            System.out.println();
            System.out.println("Please Enter File Name To Read From, (e.g. java NetworkAnalysis <filename.txt>)");
            System.out.println();
            System.exit(0);
 
        }
        insert(args[0]);
        Scanner scanner = new Scanner(System.in);
        String input;
        quit:
        while (true) {
            System.out.println();
            System.out.println(" -----------------------------------------------------------------------------------------");
            System.out.println("| Please Select from the following options:                                               |");
            System.out.println("|                                                                                         |");
            System.out.println(
                    "|   1. Find the lowest latency path and its available bandwidth                           |\n" +
                    "|   2. Determine copper connectivity                                                      |\n" +
                    "|   3. Determine whether the network would remain connected even if any two vertices fail |\n" +
                    "|   4. quit                                                                               |");
            System.out.println(" -----------------------------------------------------------------------------------------");
            System.out.println();
            System.out.print("Enter option number from 1 to 4 : ");
            input = scanner.next();
            System.out.println("------------------------------------------------------");


            if (!Pattern.matches("[1-4]", input)) {
                System.out.println("Invalid input. Please choose options form 1 to 4");
                continue;
            }

            switch (input.charAt(0)) {
                case '4':
                    break quit;
                case '1':
                    lowestLatency();
                    break;
                case '2':
                    CopperConnected();
                    break;
                case '3':
                    survive();
                    break;
            }
        }
        System.out.println("    -( Good Bye )-  ");
        System.out.println("------------------------------------------------------");

    }//MAIN

}//CLASS

