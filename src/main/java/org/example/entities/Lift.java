package org.example.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.utils.RandomGenerator;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.entities.Direction.*;

@Getter
@Setter
@ToString
public class Lift {
    private static final int MIN_NUMBER_OF_FLOORS = 1;//5
    private static final int MAX_NUMBER_OF_FLOORS = 4;//20
    private static final int LIFT_CAPACITY = 5;
    private static final int MAX_INIT_NUMBER_ON_A_FLOOR = 5;//10

    private static Lift lift;

    private int numberOfFloors;
    private Direction direction;
    private int currentFloor;
    private List<Floor> floors;
    private List<Passenger> liftState;

    private Lift(int numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
        this.direction = UP;
        this.currentFloor = 0;
        this.floors = new ArrayList<>();
        this.liftState = new ArrayList<>();
        for (int floor = 0; floor < numberOfFloors; floor++) {
            List<Passenger> passengers = new ArrayList<>();
            int currentNumberOfPassengers = RandomGenerator.generateNonNegativeInt(MAX_INIT_NUMBER_ON_A_FLOOR);
            for (int j = 0; j < currentNumberOfPassengers; j++) {
                int nextFloorForPassenger = RandomGenerator.generateIntExceptingValues(MAX_NUMBER_OF_FLOORS, Set.of(floor));
                passengers.add(new Passenger(nextFloorForPassenger));
            }
            this.floors.add(Floor.of(passengers));
        }
    }

    public Lift(String path){
        try(FileReader fr = new FileReader(path)){
            Scanner scanner = new Scanner(fr);
            this.numberOfFloors = scanner.nextInt();
            this.direction = UP;
            this.currentFloor = 0;
            this.floors = new ArrayList<>();
            this.liftState = new ArrayList<>();
            for (int floor = 0; floor < numberOfFloors; floor++) {
                List<Passenger> passengers = new ArrayList<>();
                int currentNumberOfPassengers = scanner.nextInt();
                for (int j = 0; j < currentNumberOfPassengers; j++) {
                    passengers.add(new Passenger(scanner.nextInt()));
                }
                this.floors.add(Floor.of(passengers));
            }
            Collections.reverse(floors);
        } catch (IOException e){
            System.out.println("io exception caused:(");
        }
    }

    public void makeStep() {
        dropOff();
        aboard();
        if (direction == UP && isLast(currentFloor)) {
            direction = DOWN;
            performOneLift();
            return;
        }

        if (direction == DOWN && isTheFirst(currentFloor)) {
            direction = UP;
            performOneLift();
            return;
        }

        if (isFinishFloor()) {
            direction = direction == UP ? DOWN : UP;
        }
        performOneLift();
    }

    private void performOneLift() {
        currentFloor += direction.getStep();
    }

    private void aboard() {
        Floor floor = floors.get(currentFloor);
        List<Passenger> passengersToAboard = floor
                .getPassengers()
                .stream().filter(passenger -> (passenger.getFloorTo() > currentFloor) == (direction.getStep() > 0))
                .limit(LIFT_CAPACITY - liftState.size())
                .toList();

        for (Passenger p : passengersToAboard) {
            floor.removePassenger(p);
            liftState.add(p);
        }

    }

    private void dropOff() {
        List<Passenger> droppedOff = liftState.stream()
                .filter(passenger -> passenger.getFloorTo() == currentFloor+1)
                .toList();

        liftState = liftState.stream()
                .filter(passenger -> !droppedOff.contains(passenger))
                .collect(Collectors.toList());

        droppedOff.forEach(passenger -> {
            Set<Integer> set = Set.of(passenger.getFloorTo());
            int newNextFloor = RandomGenerator.generateIntExceptingValues(MAX_NUMBER_OF_FLOORS, set);
            passenger.setFloorTo(newNextFloor);
            floors.get(currentFloor).addPassenger(passenger);
        });
    }

    private boolean isFinishFloor() {
        if (liftState.size() > 0) {
            return currentFloor == liftState.stream().max(Comparator.comparingInt(Passenger::getFloorTo)).get().getFloorTo();
        }
        return false;
    }

    private boolean isTheFirst(int currentFloor) {
        return currentFloor == 0;
    }

    private boolean isLast(int currentFloor) {
        return currentFloor == numberOfFloors - 1;
    }


    public static Lift getInstance() {
        if (lift == null) {
            int floors = RandomGenerator.generateNonNegativeIntInRange(MIN_NUMBER_OF_FLOORS, MAX_NUMBER_OF_FLOORS);
            return new Lift(floors);
        }
        return lift;
    }

    public static Lift getInstance(String path) {
        if (lift == null) {
            lift = new Lift(path);
            return lift;
        }
        return lift;
    }

    public void print(){
        System.out.println("currentFloor: " + lift.getCurrentFloor());
        System.out.println("lift state: " + lift.getLiftState());
        int cnt = 0;
        for (Floor floor : lift.getFloors()) {
            System.out.println(++cnt +" == " + floor);
        }
        System.out.println("*************************************");
    }

}
