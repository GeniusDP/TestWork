package org.example.utils;

import java.util.Date;
import java.util.Random;
import java.util.Set;

public final class RandomGenerator {
    public static int generateNonNegativeInt(int upperBound){
        Random random = new Random(new Date().getTime());
        return random.nextInt(upperBound);
    }

    public static int generateIntExceptingValues(int upperBound, Set<Integer> bannedValues) {
        int result;
        do{
            result = generateNonNegativeInt(upperBound);
        }while (bannedValues.contains(result));
        return result;
    }

    public static int generateNonNegativeIntInRange(int lowerBound, int upperBound) {
        return generateNonNegativeInt(upperBound - lowerBound + 1) + lowerBound;
    }
}
