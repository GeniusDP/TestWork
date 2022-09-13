package org.example.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.example.utils.RandomGenerator;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.entities.Direction.*;

@Getter
@Setter
@ToString
@Log4j2
public class Lift {
    private static final int MIN_NUMBER_OF_FLOORS = 5;//5
    private static final int MAX_NUMBER_OF_FLOORS = 7;//20
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
        this.currentFloor = 1;
        this.floors = new ArrayList<>();
        this.liftState = new ArrayList<>();
        for (int floor = 0; floor < numberOfFloors; floor++) {
            List<Passenger> passengers = new ArrayList<>();
            int currentNumberOfPassengers = RandomGenerator.generateNonNegativeInt(MAX_INIT_NUMBER_ON_A_FLOOR);
            for (int j = 0; j < currentNumberOfPassengers; j++) {
                int nextFloorForPassenger = RandomGenerator.generatePositiveIntExceptingValues(numberOfFloors, Set.of(floor));
                passengers.add(new Passenger(nextFloorForPassenger));
            }
            this.floors.add(Floor.of(passengers));
        }
        log.info("created new Lift instance");
    }

    public Lift(String path) {
        try (FileReader fr = new FileReader(path)) {
            Scanner scanner = new Scanner(fr);
            this.numberOfFloors = scanner.nextInt();
            this.direction = UP;
            this.currentFloor = 1;
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
            log.info("created new Lift instance");
        } catch (IOException e) {
            log.error("creating of list entity was unsuccessful <= IOException caused <= {}", e.getMessage());
            System.exit(1337);
        }
    }

    public void makeStep() {
        log.info("new step has been made");
        if (direction == UP && isLast(currentFloor)) {
            direction = DOWN;
            dropOff();
            aboard();
            performOneLift();
            return;
        }

        if (direction == DOWN && isTheFirst(currentFloor)) {
            direction = UP;
            dropOff();
            aboard();
            performOneLift();
            return;
        }

        if (isFinishFloor()) {
            direction = direction == UP ? DOWN : UP;
        }
        dropOff();
        aboard();
        performOneLift();
    }

    private void performOneLift() {
        log.info("current floor of the lift is changed");
        currentFloor += direction.getStep();
    }

    private void aboard() {
        Floor floor = floors.get(currentFloor - 1);
        List<Passenger> passengersToAboard = floor
                .getPassengers()
                .stream().filter(passenger -> {
                    return direction == UP
                            ? passenger.getFloorTo() > currentFloor
                            : passenger.getFloorTo() < currentFloor;
                })
                .limit(LIFT_CAPACITY - liftState.size())
                .collect(Collectors.toList());

        for (Passenger p : passengersToAboard) {
            floor.removePassenger(p);
            liftState.add(p);
        }

    }

    private void dropOff() {
//        System.out.println("currentFloor = " + currentFloor);
        List<Passenger> droppedOff = liftState.stream()
                .filter(passenger -> passenger.getFloorTo() == currentFloor)
                .collect(Collectors.toList());

//        System.out.println("dropped off = " + droppedOff);
        liftState = liftState.stream()
                .filter(passenger -> !droppedOff.contains(passenger))
                .collect(Collectors.toList());
//        System.out.println("liftState2 : " + liftState);
//        System.out.println("----------------");
        droppedOff.forEach(passenger -> {
            Set<Integer> set = Set.of(passenger.getFloorTo());
            int newNextFloor = RandomGenerator.generatePositiveIntExceptingValues(this.numberOfFloors, set);
            passenger.setFloorTo(newNextFloor);
            floors.get(currentFloor - 1).addPassenger(passenger);
        });
    }

    private boolean isFinishFloor() {
        if (liftState.size() > 0) {
            if (direction == UP) {
                return currentFloor == liftState.stream().max(Comparator.comparingInt(Passenger::getFloorTo)).get().getFloorTo();
            }
            return currentFloor == liftState.stream().min(Comparator.comparingInt(Passenger::getFloorTo)).get().getFloorTo();
        }
        return false;
    }

    private boolean isTheFirst(int currentFloor) {
        return currentFloor == 1;
    }

    private boolean isLast(int currentFloor) {
        return currentFloor == numberOfFloors;
    }


    public static Lift getInstance() {
        if (lift == null) {
            int floors = RandomGenerator.generateNonNegativeIntInRange(MIN_NUMBER_OF_FLOORS, MAX_NUMBER_OF_FLOORS);
            lift = new Lift(floors);
            log.info("initialized new Lift instance (with random data)");
            return lift;
        }
        log.info("used an old Lift instance (with random data)");
        return lift;
    }


    /*
     *
     * Is used for testing
     * */
    public static Lift getInstance(String path) {
        if (lift == null) {
            lift = new Lift(path);
            log.info("used an old Lift instance(from file)");
            return lift;
        }
        log.info("used an old Lift instance (from a file)");
        return lift;
    }


}
