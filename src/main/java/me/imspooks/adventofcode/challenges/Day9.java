package me.imspooks.adventofcode.challenges;

import lombok.RequiredArgsConstructor;
import me.imspooks.adventofcode.challenges.interfaces.Day;
import me.imspooks.adventofcode.challenges.interfaces.Part;
import me.imspooks.adventofcode.util.InputReader;
import me.imspooks.adventofcode.util.Pair;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class Day9 implements Day {

    private List<String> lines;
    private int[][] map;

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-9/input").getTextLines(StandardCharsets.UTF_8);

        // Create our int map
        map = new int[this.lines.get(0).length()][this.lines.size()];

        // Convert our input into integers in the map above
        for (int i = 0; i < this.lines.size(); i++) {
            String line = this.lines.get(i);
            String[] numbers = line.split("");

            for (int c = 0; c < numbers.length; c++) {
                this.map[i][c] = Integer.parseInt(numbers[c]);
            }
        }
    }

    @Part(part = 1)
    public int firstPart() {
        int result = 0;

        // Loop through each position
        for (int x = 0; x < this.map.length; x++) {
            for (int y = 0; y < this.map[x].length; y++) {
                final int current = this.map[x][y];

                // Get the directions we can check, we can only check our upper wall if we aren't at the top for example
                Set<Direction> directions = new HashSet<>();

                // Upper wall
                if (x != 0) {
                    directions.add(Direction.NORTH);
                }
                // Bottom wall
                if (x != this.map.length - 1) {
                    directions.add(Direction.SOUTH);
                }

                // Left wall
                if (y != 0) {
                    directions.add(Direction.WEST);
                }

                // Right wall
                if (y != this.map[x].length - 1) {
                    directions.add(Direction.EAST);
                }

                // Remove direction if the value is higher than our current value
                int finalX = x, finalY = y;
                directions.removeIf(direction -> {
                    try {
                        return this.map[finalX + direction.x][finalY + direction.y] > current;
                    } catch (Exception e) {
                        System.out.println("finalX = " + finalX);
                        System.out.println("finalY = " + finalY);
                        System.out.println("direction = " + direction);
                        return false;
                    }
                });

                // If the directions are empty, add to our result
                if (directions.isEmpty()) {
                    result += current + 1;
                }
            }
        }

        return result;
    }

    @Part(part = 2)
    public int secondPart() {
        // Keep track of how large our basin is;
        List<Pair<Integer, Integer>> basinLowest = new ArrayList<>();

        // Loop through each position
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                final int current = map[x][y];

                Set<Direction> directions = this.getDirections(x, y);

                // Remove direction if the value is higher than our current value
                int finalX = x, finalY = y;
                directions.removeIf(direction -> {
                    try {
                        return this.map[finalX + direction.x][finalY + direction.y] > current;
                    } catch (Exception e) {
                        System.out.println("finalX = " + finalX);
                        System.out.println("finalY = " + finalY);
                        System.out.println("direction = " + direction);
                        return false;
                    }
                });

                // If the directions are empty
                if (directions.isEmpty()) {
                    basinLowest.add(new Pair<>(x, y));
                }
            }
        }

        List<Set<Pair<Integer, Integer>>> allPositions = new ArrayList<>();

        List<Pair<Pair<Integer, Integer>, Integer>> basins = new ArrayList<>();

        for (Pair<Integer, Integer> pair : basinLowest) {
            Set<Pair<Integer, Integer>> positions = new HashSet<>();

            System.out.println(pair.getKey() + "," + pair.getValue() + "=" + map[pair.getKey()][pair.getValue()]);

            // Add the basin amount to our result, don't forget to add another 1 to it because it is our first point
            this.getBasinSize(pair, positions);

            basins.add(new Pair<>(pair, positions.size()));

            allPositions.add(positions);
        }

        // Sort from highest to lowest
        allPositions.sort((a, b) -> b.size() - a.size());
        basins.sort((a, b) -> b.getValue() - a.getValue());

        int result = 1;
        for (int i = 0; i < 3; i++) {
            result = result * basins.get(i).getValue();
        }


        // Create a visual map of all the basins for debugging purposses
        StringBuilder builder = new StringBuilder("");
        float colors = 50.0f;
        for (int x = 0; x < map.length; x++) {
            builder.append("<span>").append(x).append(": </span>");

            for (int y = 0; y < map[x].length; y++) {
                final int current = map[x][y];

                String css = "";

                for (int i = 0; i < allPositions.size(); i++) {
                    if (allPositions.get(i).contains(new Pair<>(x, y))) {
                        Color color = new Color(Color.HSBtoRGB((float) i % colors / colors, 1.0f, 1.0f));

                        css = "color:" + String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()) +
                            ";text-shadow:0px 1px 3px;";
                    }
                }


                builder.append("<span style=\"").append(css).append("\">").append(current).append("</span>");

            }
            builder.append("<br>");
        }

        try {
            Files.write(Paths.get("./day-9-result.html"), builder.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    /**
     * Add all basins into a hash set
     *
     * @param position The {@link Pair<Integer, Integer>} x and y position
     */
    private  Set<Pair<Integer, Integer>> getBasinSize(Pair<Integer, Integer> position, Set<Pair<Integer, Integer>> current) {
        final int x = position.getKey();
        final int y = position.getValue();

        current.add(position);

        Set<Pair<Integer, Integer>> upperDirections = this.getUpperDirections(x, y);

        for (Pair<Integer, Integer> newPos : upperDirections) {
            this.getBasinSize(newPos, current);
        }

        return current;
    }

    /**
     * Get if the current position has an adjacent that is 1 higher
     *
     * @param x Our current x position
     * @param y Our current y position
     * @return A {@link Set<Direction>} of the adjacent directions
     */
    private Set<Pair<Integer, Integer>> getUpperDirections(int x, int y) {
        Set<Direction> directions = this.getDirections(x, y);

        directions.removeIf(direction -> {
            try {
                return map[x + direction.x][y + direction.y] - map[x][y] != 1;
            } catch (Exception e) {
                System.out.println("finalX = " + x);
                System.out.println("finalY = " + y);
                System.out.println("direction = " + direction);
                return false;
            }
        });

        return directions.stream().map(dir -> new Pair<>(x + dir.x, y + dir.y)).collect(Collectors.toSet());
    }

    /**
     * Get the directions we can check in our map
     *
     * @param x Our current x position
     * @param y Our current y position
     * @return A {@link Set<Direction>} of the adjacent directions
     */
    private Set<Direction> getDirections(int x, int y) {
        // Get the directions we can check, we can only check our upper wall if we aren't at the top for example
        Set<Direction> directions = new HashSet<>();

        // Upper wall
        if (x != 0) {
            directions.add(Direction.NORTH);
        }
        // Bottom wall
        if (x != map.length - 1) {
            directions.add(Direction.SOUTH);
        }

        // Left wall
        if (y != 0) {
            directions.add(Direction.WEST);
        }

        // Right wall
        if (y != map[x].length - 1) {
            directions.add(Direction.EAST);
        }
        return directions;
    }

    @RequiredArgsConstructor
    public enum Direction {
        NORTH(-1, 0),
        EAST(0, 1),
        SOUTH(1, 0),
        WEST(0, -1),
        ;

        // Relative values from our current position
        private final int x;
        private final int y;
    }
}
