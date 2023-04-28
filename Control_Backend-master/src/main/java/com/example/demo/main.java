package com.example.demo;
import java.util.Set;
import java.util.HashSet;

import java.util.ArrayList;

public class main {
    public static  void main(String args[]){

       //example 1 in lecture assume :h1=1,-h2=-1,g1=1,g2=2,g3=3,g4=4
        int[][] arr = {
                {0,1,0,0,0,0},
                {-1,0,1,1,0,0},
                {0,0,0,1,0,0},
                {0,-1,-1,0,1,0},
                {0,0,0,0,0,1},
                {0,0,0,0,-1,0},
        };
        Graph graph = new Graph(arr);
        graph.solve(0,5);
    }
}
/*
                {0,1,0,1},
                {0,0,1,0},
                {0,-1,0,1},
                {0,0,0,0},
*/