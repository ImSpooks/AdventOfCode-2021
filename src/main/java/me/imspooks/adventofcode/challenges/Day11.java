package me.imspooks.adventofcode.challenges;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.imspooks.adventofcode.challenges.interfaces.Day;
import me.imspooks.adventofcode.challenges.interfaces.Part;
import me.imspooks.adventofcode.util.InputReader;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class Day11 implements Day {

    private List<String> lines;
    private Squid[][] map;

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-11/input").getTextLines(StandardCharsets.UTF_8);

        // Create our int map
        this.map = new Squid[this.lines.get(0).length()][this.lines.size()];

        // Convert our input into integers in the map above
        for (int x = 0; x < this.lines.size(); x++) {
            String line = this.lines.get(x);
            String[] numbers = line.split("");

            for (int y = 0; y < numbers.length; y++) {
                this.map[x][y] = new Squid(x, y, Integer.parseInt(numbers[y]));
            }
        }
    }

    @Part(part = 1)
    public int firstPart() {
        int totalCharges = 0;
        for (int i = 0; i < 100; i++) {
//            System.out.println(i + " = " + totalCharges);
//            {   // debug
//                System.out.println("Step " + i);
//
//                for (int x = 0; x < this.map.length; x++) {
//                    StringBuilder str = new StringBuilder();
//                    for (int y = 0; y < this.map[x].length; y++) {
//                        str.append(this.map[x][y].charge);
//                    }
//                    System.out.println(str);
//                }
//                System.out.println("");
//                System.out.println("");
//            }


            Map<Squid, Boolean> flash = new HashMap<>();

            // Add a charge to every squid
            for (int x = 0; x < this.map.length; x++) {
                for (int y = 0; y < this.map[x].length; y++) {
                    Squid squid = this.map[x][y];

                    // Check if the squid can flash
                    if (squid.charge == 9) {
                        squid.charge = 0;
                        flash.put(squid, false);
                    } else {
                        squid.charge++;
                    }
                }
            }

            // Loop through every squid that hasn't been handles yet
            while (flash.values().stream().anyMatch(b -> !b)) {
                // Copy our keyset to a list to prevent a ConcurrentModificationException
                for (Squid squid : new ArrayList<>(flash.keySet())) {
                    // Only handle squids if they aren't handled yet
                    if (!flash.get(squid)) {

                        for (Direction direction : Direction.CACHE) {
                            try {
                                Squid neighbour = this.map[squid.x + direction.x][squid.y + direction.y];

                                // Ignore squids that already have charged
                                if (neighbour.charge == 0)
                                    continue;

                                // Check if the squid can flash
                                if (neighbour.charge == 9) {
                                    neighbour.charge = 0;
                                    flash.put(neighbour, false);
                                } else {
                                    neighbour.charge++;
                                }
                            } catch (IndexOutOfBoundsException ignored) {
                                // Ignore this
                            }
                        }

                        // Set handled to true
                        flash.put(squid, true);
                    }
                }
            }

            totalCharges += flash.size();
        }

        return totalCharges;
    }

    @Part(part = 2)
    public int secondPart() {
        // Endlessly loop, we can get our step by returning 'i'
        for (int i = 1; true; i++) {

            Map<Squid, Boolean> flash = new HashMap<>();

            // Add a charge to every squid
            for (int x = 0; x < this.map.length; x++) {
                for (int y = 0; y < this.map[x].length; y++) {
                    Squid squid = this.map[x][y];

                    // Check if the squid can flash
                    if (squid.charge == 9) {
                        squid.charge = 0;
                        flash.put(squid, false);
                    } else {
                        squid.charge++;
                    }
                }
            }

            // Loop through every squid that hasn't been handles yet
            while (flash.values().stream().anyMatch(b -> !b)) {
                // Copy our keyset to a list to prevent a ConcurrentModificationException
                for (Squid squid : new ArrayList<>(flash.keySet())) {
                    // Only handle squids if they aren't handled yet
                    if (!flash.get(squid)) {

                        for (Direction direction : Direction.CACHE) {
                            try {
                                Squid neighbour = this.map[squid.x + direction.x][squid.y + direction.y];

                                // Ignore squids that already have charged
                                if (neighbour.charge == 0)
                                    continue;

                                // Check if the squid can flash
                                if (neighbour.charge == 9) {
                                    neighbour.charge = 0;
                                    flash.put(neighbour, false);
                                } else {
                                    neighbour.charge++;
                                }
                            } catch (IndexOutOfBoundsException ignored) {
                                // Ignore this
                            }
                        }

                        // Set handled to true
                        flash.put(squid, true);
                    }
                }
            }

            // Return the step we have 100 flashes
            if (flash.size() == 100) {
                return i;
            }
        }
    }

    @Getter
    @Setter
    @ToString
    private static class Squid {
        private final int x;
        private final int y;
        private int charge;

        public Squid(int x, int y, int charge) {
            this.x = x;
            this.y = y;
            this.charge = charge;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Squid)) return false;
            Squid squid = (Squid) o;
            return x == squid.x &&
                    y == squid.y &&
                    charge == squid.charge;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    @RequiredArgsConstructor
    public enum Direction {
        NORTH_WEST(-1, -1),
        NORTH(-1, 0),
        NORTH_EAST(-1, 1),

        EAST(0, 1),

        SOUTH_EAST(1, 1),
        SOUTH(1, 0),
        SOUTH_WEST(1, -1),

        WEST(0, -1),
        ;

        public static final Direction[] CACHE = values();

        // Relative values from our current position
        private final int x;
        private final int y;

        public static Set<Day9.Direction> getDirections() {
            return Arrays.stream(Day9.Direction.values()).collect(Collectors.toSet());
        }
    }
}
