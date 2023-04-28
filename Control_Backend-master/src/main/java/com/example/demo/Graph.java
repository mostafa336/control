package com.example.demo;
import java.util.Set;
import java.util.HashSet;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.*;

@Component
public class Graph {

    /**Talha Puts his arrays here and functions down **/
    /**Tefa uses them and gives us the c*/
    private enum visit {NOT_VISITED, IN_STACK, VISITED};
    private ArrayList<StringBuilder> arrayList = new ArrayList(); // Loops names, start from  1
    private ArrayList<Integer> arryList = new ArrayList();        // Loops gains , start form  1
    private HashMap<String, Integer> loops = new HashMap();
    private HashMap<String, Integer> loopsCheck = new HashMap();
    private int n;
    private visit[] visited = new visit[1000];
    private int[][] graph = new int[1000][1000];
    private boolean[][] relations;
    private ArrayList<ArrayList<Integer>> nonTouchingC = new ArrayList<>(); //start from 1
    private ArrayList<ArrayList<Integer>> FP = new ArrayList();        // forward_paths
    private ArrayList<StringBuilder> forward_paths = new ArrayList();  //start from zero , forward path here
    private ArrayList<Integer> Path_gains = new ArrayList<>();         //gains of forward paths
    private int[][] GMatrix;
    public Graph(int[][] g)
    {
        GMatrix = g;
        n = g.length;
        for(int i = 0; i < 1000; ++i) {
            for(int j = 0; j < 1000; ++j) {
                graph[i][j] = 0;
            }
        }
        for(int i = 1; i <= n; i++)
        {
            for(int j = 1; j <= n; j++)
            {
                graph[i][j]=g[i-1][j-1];
            }
        }
    }
    public ArrayList<ArrayList<Integer>> solve(int s, int e)
    {
        findCycles();
        findNontouching();
        findCombinations();
        FindForwaDPaths(s,e);
        Cal_Gains();
        /*Call Your functions here ya regala**/
        System.out.println("transfer function = " + calcTF());
        ArrayList<ArrayList<Integer>> aykalam = new ArrayList<>();
        return aykalam;
    }
    private  double  calcTF(){
        double result=0;
        for(int i=0;i<Path_gains.size();i++){
            int delt = calcFPDelta(i);
            System.out.println("forward path : "+forward_paths.get(i)+" ,gain = "+Path_gains.get(i)+ " ,delta of it = "+delt );
            result+=(Path_gains.get(i)*delt);
        }
        System.out.println("Result of summation = "+ result);
        int delta=calcDenDelta();
        System.out.println("DELTAAAAAAAAAAA = "+delta);
        result/=delta;
        return result;
    }
    private int  calcDenDelta(){
        int delta=1;
        for (int i = 0; i < nonTouchingC.size(); i++) {
            ArrayList<Integer> loop = nonTouchingC.get(i);
            int rowSize = loop.size();
            int tmp = 1;

        //    System.out.print("LOOP IS "+loop +" ,size = "+rowSize+"   k: ");
            for (int j = 0; j < rowSize; j++) {
                int k = loop.get(j).intValue();
           //     System.out.print(k);
                tmp *= arryList.get(k-1);
            }
         //   System.out.println();
            if (rowSize % 2 == 1) {
                delta -= tmp;
            } else {
                delta += tmp;
            }
        }

        return delta;
    }
    private int calcFPDelta(int fp){
        //get the touching loops
        StringBuilder path = forward_paths.get(fp);
        String[] pathArray = path.toString().replaceAll("[\\[\\]]", "").split(", ");
        ArrayList<Integer>TouchingLoops= new ArrayList<>();
        Set<Integer> mySet = new HashSet<>();
        // System.out.println("################calcfd#####################");
        //System.out.println("path   :"+path+" ,fb  : "+fp);
        for(int i = 0; i < arrayList.size(); i++){
            StringBuilder sb = arrayList.get(i);
            //System.out.println("sb   : "+sb);
            for(int j = 0; j < sb.length(); j++){
                int tmp = sb.charAt(j)-'0';
                //System.out.print("tmp   : "+tmp+"  ,tmp1  : ");
                boolean break1=false;
                for(int k = 0; k < pathArray.length; k++){
                    int tmp1 = Integer.parseInt(pathArray[k]);
                    //System.out.print(tmp1+" ");
                    if(tmp==tmp1){
                        mySet.add(i+1);  //i+1 as first loop is in index zero
                        break1=true;
                        break;
                    }
                }
                //System.out.println();
                if(break1){break;}
            }
        }
      //  System.out.println("\n###################DONEEEEEEEEEEEEEE#####################");
      //  System.out.print("TOUCHING LOOOPS : ");
        for (int i : mySet) {
    //        System.out.print(i+"  ");
            TouchingLoops.add(i);
        }
    //    System.out.println("");
        //calculate delta
        int del=1;
        for (int i = 0; i < nonTouchingC.size(); i++) {
            ArrayList<Integer> loop = nonTouchingC.get(i);
            int rowSize = loop.size();
            int tmp = 1;
            boolean flag=true;

          //  System.out.println("\n LOOOPPPP :: "+ loop + "  rowSize : "+rowSize );

            for (int j = 0; j < rowSize; j++) {
                boolean match =false;
                int q = loop.get(j).intValue();
                tmp *= arryList.get(q-1);
              //  System.out.print("q : "+q+" ,x : ");
                for(int k=0;k < TouchingLoops.size() ; k++) {
                    int x=TouchingLoops.get(k) ;
                 //   System.out.print(x+" ");
                    if(x==q) {
                        match = true;
                        break;
                    }
                }
                if(match){flag=false;break;}
            }
            if(flag) {
                if (rowSize % 2 == 1) {
                    del -= tmp;
                } else {
                    del += tmp;
                }
            }
        }
       // System.out.println("delta of forward path "+fp + " = "+del);
        return del;
    }

    private void findCombinations(){
        for(int i = 1; i < 1<<arrayList.size(); i++) // 2*n
        {
            ArrayList<Integer> in = new ArrayList<>();
            for(int j = 0; j < arrayList.size(); j++)
            {
                if(((1<<j)&i)!=0)
                    in.add(j+1);
            }
            boolean nonTouching = true;
            for(int x : in)
            {
                for(int y : in)
                {
                    if(x==y) continue;
                    nonTouching &= !relations[x][y];
                }
            }
            if(nonTouching)
                nonTouchingC.add(in);
        }
        System.out.print("NON TOUCHING LOOPS :: ");
        for(int i = 0; i < nonTouchingC.size(); i++)
        {
            for(int j = 0; j < nonTouchingC.get(i).size(); j++)
            {
                System.out.print(nonTouchingC.get(i).get(j) + " ");
            }
            System.out.println();
        }

    }
    private void findNontouching()
    {
        int[][] bit_map = new int[arrayList.size()+1][n+1];
        relations = new boolean[arrayList.size()+1][arrayList.size()+1];
        int it = 1;
        for(StringBuilder iter: arrayList)
        {
            for(int i = 0; i < iter.length(); i++)
            {
                bit_map[it][(iter.charAt(i)-'0')] = 1;
            }
            it++;
        }
        // 0 0 1 0 0
        /**
         * 0000
         * 0100
         * 0101
         * 0110*/
        for(int i = 1; i <= arrayList.size(); i++)
        {
            for(int j = i+1; j <= arrayList.size(); j++)
            {
                int x = 0;
                for(int k = 1; k <= n; k++)
                {
                    x |= (bit_map[i][k]&bit_map[j][k]);
                }
                if(x!=0) // found intersection
                {
                    relations[i][j] = true;
                    relations[j][i] = true;
                }
            }
        }
//        for(int i = 1; i <= arrayList.size(); i++)
//        {
//            for(int j = 1; j <= arrayList.size(); j++)
//            {
//                System.out.print(relations[i][j] + " ");
//            }
//            System.out.println();
//        }
    }
    private void findCycles() {
        int i;
        for(i = 0; i < 1000; ++i) {
            this.visited[i] = visit.NOT_VISITED;
        }
        for(i = 1; i <= n; ++i) {
            Stack<Integer> stack = new Stack();
            stack.push(i);
            this.visited[i] = visit.IN_STACK;
            this.processDFSTree(stack, n, 1);
        }
        for(i = 0; i < arrayList.size(); ++i) {
            PrintStream var10000 = System.out;
            Object var10001 = arrayList.get(i);
            var10000.println(var10001 + " " + arryList.get(i));
        }

    }
    private String sortString(String inputString) {
        char[] tempArray = inputString.toCharArray();
        Arrays.sort(tempArray);
        return new String(tempArray);
    }
    private void processDFSTree(Stack<Integer> stack, int n, int gain) {
        int curr = (Integer)stack.peek();

        for(int j = 1; j <= n; ++j) {
            if (this.graph[curr][j] != 0) {
                if (this.visited[j] == visit.IN_STACK) {
                    stack.push(j);
                    this.recordStack(stack, gain*this.graph[curr][j]);
                } else if (this.visited[curr] != visit.VISITED) {
                    stack.push(j);
                    this.visited[j] = visit.IN_STACK;
                    this.processDFSTree(stack, n, gain * this.graph[curr][j]);
                    stack.pop();
                }
            }
        }
        this.visited[curr] = visit.VISITED;
    }

    private void recordStack(Stack<Integer> stack, int gain) {
        String path = new String();
        Stack<Integer> stack1 = new Stack();
        int x = (Integer)stack.pop();
        path = path + x;
        while((Integer)stack.peek() != x) {
            int y = (Integer)stack.pop();
            path = path + y;
            stack1.push(y);
        }
        String buffer = sortString(path);
        path = path + x;
        if (!loops.containsKey(buffer)) {
            loops.put(buffer, gain);
            StringBuilder a = new StringBuilder();
            a.append(path);
            a.reverse();
            arrayList.add(a);
            arryList.add(gain);
        }
        while(!stack1.empty()) {
            stack.push((Integer)stack1.pop());
        }
    }


    private void Cal_Gains(){
        for (int i = 0; i < FP.size(); i++) {
            int g = 1 ;
            for (int j = 0; j < FP.get(i).size()-1; j++) {
                g = g * GMatrix[FP.get(i).get(j)][FP.get(i).get(j+1)];
            }
            Path_gains.add(g);
        }
//        for(int i = 0; i < Path_gains.size(); i++)
//            System.out.print(Path_gains.get(i) +" ");
//        System.out.println();
    }
    private void FindForwaDPaths(int start,int end){
        boolean[] isVisited= new boolean[GMatrix.length];
        for (int i = 0; i < GMatrix.length; i++) {
            isVisited[i] = false;
        }
        ArrayList<Integer> path = new ArrayList();
        path.add(start);
        Forwad_Paths_Finder(start,end,isVisited,path);
        for (int i = 0; i < FP.size(); i++) {
            forward_paths.add(new StringBuilder(FP.get(i).toString()));
        }
        System.out.print("FORWARDDDDDDDD PATHHHHHHHH :    ");
        for(int i = 0; i < forward_paths.size(); i++)
            System.out.print(forward_paths.get(i)+" ");
        System.out.println();
    }

    private void Forwad_Paths_Finder(int node, int end, boolean[] isVisited, ArrayList<Integer> path) {
        if (node == end){
            FP.add(new ArrayList<>(path));
            return;
        }
        isVisited[node] = true;
        for (int i = 0; i < GMatrix.length; i++) {
            if (GMatrix[node][i] != 0 && !isVisited[i]){
                path.add(i);
                Forwad_Paths_Finder(i,end,isVisited,path);
                path.remove(path.size()-1);
            }
        }
        isVisited[node] = false;
    }

}
