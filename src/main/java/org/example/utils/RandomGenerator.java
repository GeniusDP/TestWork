package org.example.utils;

import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.Random;
import java.util.Set;

@Log4j2
public final class RandomGenerator {
    private static Random random = new Random(new Date().getTime());

    public static int generateNonNegativeInt(int upperBound) {
        log.info("a random int from range [0,{}] has been generated", upperBound - 1);
        return random.nextInt(upperBound);
    }

    public static int generatePositiveIntExceptingValues(int upperBound, Set<Integer> bannedValues) {
        int result;
        do {
            result = random.nextInt(upperBound) + 1;
        } while (bannedValues.contains(result));
        log.info("a random int from range [0,{}] has been generated(excepting banned values)", upperBound - 1);
        return result;
    }

    public static int generateNonNegativeIntInRange(int lowerBound, int upperBound) {
        log.info("a random int from range [{},{}] has been generated(excepting banned values)",
                lowerBound, upperBound - lowerBound + 1);
        return generateNonNegativeInt(upperBound - lowerBound + 1) + lowerBound;
    }
}
