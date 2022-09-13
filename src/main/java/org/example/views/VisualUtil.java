package org.example.views;

import org.example.entities.Floor;
import org.example.entities.Lift;
import org.example.entities.Passenger;
import org.example.utils.Color;
import org.example.utils.ColorfulPrinter;

import java.util.List;
import java.util.Scanner;

import static org.example.entities.Direction.*;
import static org.example.utils.Color.*;

public class VisualUtil {
    private final Lift lift;

    public VisualUtil(Lift lift) {
        this.lift = lift;
    }

    public void print() {
        final int WIDTH_OF_PRINT = 17;
        String directionChar = lift.getDirection() == UP ? "^" : "v";
        List<Floor> floors = lift.getFloors();
        for (int i = floors.size()-1; i >= 0 ; i--) {
            if( i + 1 == lift.getCurrentFloor() ){
                ColorfulPrinter.printColorfullyAndReset(ANSI_RED, "|");
                ColorfulPrinter.printColorfullyAndReset(ANSI_BLUE, directionChar);
                String result = "";
                for (Passenger passenger : lift.getLiftState()) {
                    result += String.format("%3s", passenger);
                }
                while (result.length() < 15){
                    result += " ";
                }
                System.out.print(result);
                ColorfulPrinter.printColorfullyAndReset(ANSI_BLUE, directionChar);
                ColorfulPrinter.printColorfullyAndReset(ANSI_RED, "|");
                ColorfulPrinter.printlnColorfullyAndReset(ANSI_YELLOW, floors.get(i).toString());
            }else{
                ColorfulPrinter.printColorfullyAndReset(ANSI_RED, "|");
                for (int j = 0; j < WIDTH_OF_PRINT; j++) {
                    System.out.print(" ");
                }
                ColorfulPrinter.printColorfullyAndReset(ANSI_RED, "|");
                ColorfulPrinter.printlnColorfullyAndReset(ANSI_YELLOW, floors.get(i).toString());
            }
        }

        System.out.println("*************************************");
    }

    public boolean whetherToContinue(){
        String text = "Want to stop? Type in \"q\"(without quotes) or else you will continue";
        ColorfulPrinter.printlnColorfullyAndReset(Color.ANSI_GREEN, text);
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        return !line.equals("q");
    }

}
