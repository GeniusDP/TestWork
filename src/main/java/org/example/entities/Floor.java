package org.example.entities;

import lombok.ToString;

import java.util.List;

public class Floor {
    private final List<Passenger> passengers;

    public Floor(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public static Floor of(List<Passenger> passengers) {
        return new Floor(passengers);
    }

    public void addPassenger(Passenger newPassenger) {
        if (!passengers.contains(newPassenger)){
            passengers.add(newPassenger);
        }
    }

    public void removePassenger(Passenger removePassenger) {
        passengers.remove(removePassenger);
    }

    public List<Passenger> getPassengers(){
        return passengers;
    }

    @Override
    public String toString() {
        String result = "";
        for (Passenger passenger : passengers) {
            result += passenger + " ";
        }
        return result;
    }
}
