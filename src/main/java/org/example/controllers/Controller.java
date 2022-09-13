package org.example.controllers;

import org.example.entities.Lift;
import org.example.views.VisualUtil;

public class Controller {

    public void start() {
        Lift lift = Lift.getInstance();
        VisualUtil visualUtil = new VisualUtil(lift);
        visualUtil.print();
        while (visualUtil.whetherToContinue()){
            lift.makeStep();
            visualUtil.print();
        }
    }

}
