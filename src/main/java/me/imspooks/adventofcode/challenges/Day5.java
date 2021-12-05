package me.imspooks.adventofcode.challenges;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.imspooks.adventofcode.challenges.interfaces.Day;
import me.imspooks.adventofcode.challenges.interfaces.Part;
import me.imspooks.adventofcode.util.InputReader;
import me.imspooks.adventofcode.util.Pair;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class Day5 implements Day {

    private static final Pattern REGEX = Pattern.compile("^(\\d+),(\\d+) -> (\\d+),(\\d+)$");

    private List<String> lines;

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-5/input").getTextLines(StandardCharsets.UTF_8);
    }

    @Part(part = 1)
    public int firstPart() {
        // Make a list with pairs (x1,y1) and (x2,y2)
        List<Pair<Point, Point>> pairList = new ArrayList<>();

        int width = 0;
        int height = 0;

        for (String line : this.lines) {
            // See if the regex matches
            Matcher matcher = REGEX.matcher(line);

            while (matcher.find()) {
                if (matcher.groupCount() == 4) {
                    int x1 = Integer.parseInt(matcher.group(1));
                    int y1 = Integer.parseInt(matcher.group(2));

                    int x2 = Integer.parseInt(matcher.group(3));
                    int y2 = Integer.parseInt(matcher.group(4));

                    // Check if it is either horizontal or vertical
                    if (x1 == x2 || y1 == y2) {
                        pairList.add(new Pair<>(new Point(x1, y1), new Point(x2, y2)));
                    }

                    // Adjust the width and height for the map
                    width = Math.max(width, Math.max(x1, x2));
                    height = Math.max(height, Math.max(y1, y2));
                }
            }
        }
        // Add one to the map size to prevent index out of bounds, not sure if this is needed, but we do it anyway
        width++;
        height++;


        int[][] map = new int[width][height];

        // Add points to the map
        for (Pair<Point, Point> pair : pairList) {
            // Get the minimum and maximum points
            int xMin = Math.min(pair.getKey().x, pair.getValue().x);
            int xMax = Math.max(pair.getKey().x, pair.getValue().x);

            int yMin = Math.min(pair.getKey().y, pair.getValue().y);
            int yMax = Math.max(pair.getKey().y, pair.getValue().y);

            for (int x = xMin; x <= xMax; x++) {
                for (int y = yMin; y <= yMax; y++) {
                    map[x][y]++;
                }
            }
        }

        int overlap = 0;

        // Check for overlapping positions
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x][y] >= 2) {
                    overlap++;
                }
            }
        }

        return overlap; // 7438
    }

    @Part(part = 2)
    public int secondPart() {
        // Make a map with pairs (x1,y1) and (x2,y2) and a diagonal state
        List<Pair<Point, Point>> pairList = new ArrayList<>();

        int width = 0;
        int height = 0;

        for (String line : this.lines) {
            // See if the regex matches
            Matcher matcher = REGEX.matcher(line);

            while (matcher.find()) {
                if (matcher.groupCount() == 4) {
                    int x1 = Integer.parseInt(matcher.group(1));
                    int y1 = Integer.parseInt(matcher.group(2));

                    int x2 = Integer.parseInt(matcher.group(3));
                    int y2 = Integer.parseInt(matcher.group(4));

                    // Add it to the map, the boolean state is whether it is diagonal
                    pairList.add(new Pair<>(new Point(x1, y1), new Point(x2, y2)));

                    // Adjust the width and height for the map
                    width = Math.max(width, Math.max(x1, x2));
                    height = Math.max(height, Math.max(y1, y2));
                }
            }
        }
        // Add one to the map size to prevent index out of bounds, not sure if this is needed, but we do it anyway
        width++;
        height++;


        int[][] map = new int[width][height];

        // Add points to the map
        for (Pair<Point, Point> pair : pairList) {
            // Check differences for each coordinate
            int xDiff = pair.getValue().x - pair.getKey().x;
            int yDiff = pair.getValue().y - pair.getKey().y;

            int maxDiff = Math.max(Math.abs(xDiff), Math.abs(yDiff));

            for (int i = 0; i <= maxDiff; i++) {
                int x = pair.getKey().x;
                int y = pair.getKey().y;

                // Add values to x and y
                if (xDiff != 0) {
                    x += (xDiff > 0 ? i : -i);
                }
                if (yDiff != 0) {
                    y += (yDiff > 0 ? i : -i);
                }

                map[x][y]++;
            }
        }

        int overlap = 0;

        // Check for overlapping positions
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x][y] >= 2) {
                    overlap++;
                }
            }
        }

        return overlap;
    }

    @ToString
    @EqualsAndHashCode
    @RequiredArgsConstructor
    private static class Point {
        private final int x;
        private final int y;
    }
}
