package me.imspooks.adventofcode.challenges;

import lombok.RequiredArgsConstructor;
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
        return this.getLowestPoints().stream().map(p -> p.value + 1).reduce(0, Integer::sum);
    }

    @Part(part = 2)
    public int secondPart() {
        // Convert all points to a set
        List<Set<BasinPoint>> basins = this.getLowestPoints().stream().map(p -> {
            Set<BasinPoint> points = new HashSet<>();
            points.add(p);
            return points;
        }).collect(Collectors.toList());

        int MAX_VALUE = 9;

        boolean allHandled = false;

        // Check if every point has been handled
        while (!allHandled) {
            for (Set<BasinPoint> basinPoint : basins) {
                Set<BasinPoint> pointsToAdd = new HashSet<>();

                for (BasinPoint point : basinPoint) {
                    int x = point.x;
                    int y = point.y;

                    // Loop through each direction
                    for (Direction direction : Direction.getDirections()) {
                        try {
                            int newX = x + direction.x;
                            int newY = y + direction.y;
                            int value = map[newX][newY];

                            BasinPoint newPoint = new BasinPoint(value, newX, newY);

                            // Check if the point isn't the same as the max value and add it
                            if (map[newPoint.x][newPoint.y] != MAX_VALUE) {
                                pointsToAdd.add(newPoint);
                            }
                        } catch (IndexOutOfBoundsException ignored) {
                            // We do not care if this happens
                        }

                        point.handled = true;
                    }
                }

                // Add the new points to our set
                basinPoint.addAll(pointsToAdd);
            }

            // Set if every point has been handled or not
            allHandled = basins.stream().flatMap(Set::stream).allMatch(p -> p.handled);
        }

        basins.sort((a, b) -> b.size() - a.size());
        int result = 1;
        for (int i = 0; i < 3; i++) {
            result = result * basins.get(i).size();
        }

        // Create a visual map of all the basins for debugging purposes
//        StringBuilder builder = new StringBuilder("");
//        float colors = 50.0f;
//        float brightness = 12.0f;
//        for (int x = 0; x < map.length; x++) {
//            builder.append("<span>").append(x).append(": </span>");
//
//            for (int y = 0; y < map[x].length; y++) {
//                final int current = map[x][y];
//
//                String css = "";
//
//                for (int i = 0; i < basins.size(); i++) {
//                    if (basins.get(i).contains(new BasinPoint(x, y))) {
//                        Color color = new Color(Color.HSBtoRGB((float) i % colors / colors, 1.0f, 0.5f + (i % brightness / brightness) * 0.5f));
//
//                        css = "color:" + String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()) +
//                                ";text-shadow:0px 1px 3px;";
//                    }
//                }
//
//                builder.append("<span style=\"").append(css).append("\">").append(current).append("</span>");
//
//            }
//            builder.append("<br>");
//        }
//
//        try {
//            Files.write(Paths.get("./day-9-result.html"), builder.toString().getBytes(StandardCharsets.UTF_8));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return result;
    }

    public List<BasinPoint> getLowestPoints() {
        List<BasinPoint> points = new ArrayList<>();

        // Loop through each position
        for (int x = 0; x < this.map.length; x++) {
            for (int y = 0; y < this.map[x].length; y++) {
                final int current = this.map[x][y];

                // Get the directions we can check, we can only check our upper wall if we aren't at the top for example
                Set<Direction> directions = Direction.getDirections();

                // Remove direction if the value is higher than our current value
                int finalX = x, finalY = y;
                directions.removeIf(direction -> {
                    try {
                        return this.map[finalX + direction.x][finalY + direction.y] > current;
                    } catch (IndexOutOfBoundsException e) {
                        return true;
                    }
                });

                // If the directions are empty, add to our result
                if (directions.isEmpty()) {
                    points.add(new BasinPoint(current, x, y));
                }
            }
        }

        return points;
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

        public static Set<Direction> getDirections() {
            return Arrays.stream(Direction.values()).collect(Collectors.toSet());
        }
    }

    @RequiredArgsConstructor
    private static class BasinPoint {
        private final int value;
        private final int x;
        private final int y;
        private boolean handled = false;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BasinPoint that = (BasinPoint) o;
            return value == that.value && x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, x, y);
        }
    }
}
