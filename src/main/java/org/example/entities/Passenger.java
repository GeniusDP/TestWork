package org.example.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Passenger {
    private static long idSequence = 0;
    private final long id;
    private int floorTo;

    public Passenger(int floorTo) {
        this.id = idSequence++;
        this.floorTo = floorTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return id == passenger.id && floorTo == passenger.floorTo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, floorTo);
    }

    public Passenger clonePassenger() {
        return new Passenger(floorTo);
    }


    @Override
    public String toString() {
        return "" + floorTo;
    }
}
