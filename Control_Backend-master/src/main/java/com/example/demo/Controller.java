package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Component
@RestController
@CrossOrigin

public class Controller {
    @ResponseBody
    @RequestMapping(value = "/addshape/{s}/{e}",method = RequestMethod.POST)
    public ArrayList<ArrayList<Integer>> addShape(@RequestBody int[][] g, @PathVariable int s, @PathVariable int e) {
        Graph graph = new Graph(g);
        return graph.solve(s, e);
    }

}
