package org.example.controllers;

import org.example.entities.Lift;

public class Controller {

    public void start(){
        Lift lift = Lift.getInstance("src/main/resources/input.txt");
        lift.print();
        for (int i = 0; i < 20; i++) {
            lift.makeStep();
            lift.print();
        }
    }

}
