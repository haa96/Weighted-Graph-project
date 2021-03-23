public class DepthFirstSearch {
    private boolean[] marked ;    // marked[v] = is there an s-v path?
    private int count;           // number of vertices connected to s

    public DepthFirstSearch(EdgeWeightedGraph G){
        marked = new boolean[G.V];
    }


    private void dfs(EdgeWeightedGraph G , int s){
        if (marked[s]) return;

        else
            marked[s]= true;
        for(Edge e : G.edges()){
            int v = e.either();
            int w = e.other(v);
            if (v != s && w != s) {
                continue;
            }
            if (e.type.equals("copper")) 
                dfs(G,e.other(s));
        }
    }


    public boolean copperConnected(EdgeWeightedGraph G){
        dfs(G,0);
        for (int i = 0 ; i < marked.length; i++){
            if (!marked[i]) return false;
        }
        return true;
    }
    public boolean fail(EdgeWeightedGraph G){
        for (int i =0 ; i < G.V; i ++ ){
            for(int j = 0 ; j < G.V; j++ ){
                if(i == j)continue;
                boolean[] marked = new boolean[G.V];
                if (!vertexFail(G,(i+j+1)%G.V , i, j, marked))
                return true;
            }
        }
        return false;
    }
    private boolean vertexFail(EdgeWeightedGraph G , int s, int i , int j, boolean[] marked){
        if (marked[s]) {
            return check_marked(marked, i, j); // i and j is the ignored vertices
        }
        else
            marked[s]= true;
        for(Edge e : G.edges()){
            int v = e.either();
            int w = e.other(v);
            if (v != s && w != s ) continue;
            if(e.other(s) == i || e.other(s)==j) continue;
            vertexFail(G,e.other(s),i,j,marked);
        }
        return check_marked(marked, i, j); // i and j is the ignored vertices
    }

    public boolean check_marked(boolean[] marked, int i, int j) {
        for (int x = 0 ; x < marked.length; x ++){
            if(x == i || x == j)continue;
            if(!marked[x]) return false;
        }
        return true;
    }

    


}
