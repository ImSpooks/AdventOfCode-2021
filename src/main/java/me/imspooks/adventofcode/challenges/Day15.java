package me.imspooks.adventofcode.challenges;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.imspooks.adventofcode.challenges.interfaces.Day;
import me.imspooks.adventofcode.challenges.interfaces.Part;
import me.imspooks.adventofcode.util.InputReader;
import me.imspooks.adventofcode.util.Position;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class Day15 implements Day {

    private List<String> lines;

    private int[][] map;

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-15/input").getTextLines(StandardCharsets.UTF_8);

        // Create our int map
        this.map = new int[this.lines.get(0).length()][this.lines.size()];

        // Convert our input into integers in the map above
        for (int x = 0; x < this.lines.size(); x++) {
            String line = this.lines.get(x);
            String[] numbers = line.split("");

            for (int y = 0; y < numbers.length; y++) {
                this.map[x][y] = Integer.parseInt(numbers[y]);
            }
        }
    }

    @Part(part = 1)
    public int firstPart() {
        // Our target positions
        Position<Integer> topLeft = new Position<>(0, 0);
        Position<Integer> bottomRight = new Position<>(this.map.length - 1, this.map[0].length - 1);

        // Get our charged path
        ChargedPath path = this.getLowestCostPath(topLeft, bottomRight, this.map);

        return path.getCharge();
    }

    @Part(part = 2)
    public int secondPart() {
        // Copy our map
        int[][] map = new int[this.map.length * 5][this.map[0].length * 5];

        // Increase our map 5 times, but each time we add 'i' to each value

        // First we do this for the x-axis
        for (int i = 0; i < 5; i++) {
            for (int x = 0; x < this.map.length; x++) {
                for (int y = 0; y < this.map[x].length; y++) {

                    // Wrap our value between 1 and 9
                    int value = this.map[x][y] + i;
                    while (value > 9) {
                        value -= 9;
                    }

                    map[x + (this.map.length * i)][y] = value;
                }
            }
        }

        // After that, we do this for the y-axis
        for (int i = 0; i < 5; i++) {
            for (int x = 0; x < map.length; x++) {
                for (int y = 0; y < this.map[0].length; y++) {


                    int value = map[x][y] + i;
                    while (value > 9) {
                        value -= 9;
                    }

                    map[x][y + (this.map.length * i)] = value;
                }
            }
        }

        // Our target positions
        Position<Integer> topLeft = new Position<>(0, 0);
        Position<Integer> bottomRight = new Position<>(map.length - 1, map[0].length - 1);

        // Get our charged path
        ChargedPath path = this.getLowestCostPath(topLeft, bottomRight, map);

        return path.getCharge(); // 2955
    }

    private ChargedPath getLowestCostPath(Position<Integer> position, Position<Integer> end, int[][] map) {
        // Create our queue and keep track of positions whe visited
        Queue<ChargedPath> queue = new PriorityQueue<>(); // Queue with a comparator, that compares the polls the current lowest cost path
        Set<Position<Integer>> visited = new HashSet<>();

        // Add the starting position
        queue.add(new ChargedPath(position, 0));

        // Continue the loop while there are still positions in our queue and we still havent found the end
        while (!queue.isEmpty() && !queue.peek().getPosition().equals(end)) {
            ChargedPath top = queue.poll();
            if (top == null) {
                continue;
            }
            // Add the current position to our path to take
            top.getPath().add(top.getPosition());

            for (Direction direction : Direction.CACHE) {
                // Check if our neighbour is inside the map and hasn't been visited before
                Position<Integer> newPos = new Position<>(top.getPosition().getX() + direction.x, top.getPosition().getY() + direction.y);
                if (newPos.getX() < 0 || newPos.getX() >= map.length || newPos.getY() < 0 || newPos.getY() >= map[0].length) {
                    continue;
                }

                if (visited.contains(newPos)) {
                    continue;
                }

                // Create a new path instance with the new position and add the new charge on top of it
                ChargedPath path = new ChargedPath(newPos, top.getCharge() + map[newPos.getX()][newPos.getY()]);
                path.getPath().addAll(top.getPath()); // Copy effective path into the new position

                // Add visited and new path to the queue
                visited.add(path.getPosition());
                queue.add(path);
            }
        }
        return queue.peek();
    }

    @RequiredArgsConstructor
    public enum Direction {
        NORTH(-1, 0),
        EAST(0, 1),
        SOUTH(1, 0),
        WEST(0, -1),
        ;

        public static final Direction[] CACHE = values();

        // Relative values from our current position
        private final int x;
        private final int y;

        public static Set<Direction> getDirections() {
            return Arrays.stream(Direction.CACHE).collect(Collectors.toSet());
        }
    }

    @Getter
    @RequiredArgsConstructor
    private static class ChargedPath implements Comparable<ChargedPath> {
        private final List<Position<Integer>> path = new ArrayList<>();
        private final Position<Integer> position;
        private final int charge;

        @Override
        public int compareTo(ChargedPath o) {
            return Comparator.comparingInt(ChargedPath::getCharge).compare(this, o);
        }
    }
}
