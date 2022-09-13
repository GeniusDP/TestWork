package org.example.entities;

import lombok.ToString;

import java.util.List;

@ToString
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

}
