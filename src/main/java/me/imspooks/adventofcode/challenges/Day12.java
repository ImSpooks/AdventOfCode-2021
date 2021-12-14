package me.imspooks.adventofcode.challenges;

import me.imspooks.adventofcode.challenges.interfaces.Day;
import me.imspooks.adventofcode.challenges.interfaces.Part;
import me.imspooks.adventofcode.util.InputReader;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class Day12 implements Day {

    private List<String> lines;

    private Map<String, List<String>> connections = new HashMap<>();

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-12/input").getTextLines(StandardCharsets.UTF_8);

        this.lines.forEach(line -> {
            String[] positions = line.split("-");

            this.connections.putIfAbsent(positions[0], new ArrayList<>());
            this.connections.putIfAbsent(positions[1], new ArrayList<>());

            this.connections.get(positions[0]).add(positions[1]);
            this.connections.get(positions[1]).add(positions[0]);
        });
    }

    @Part(part = 1)
    public int firstPart() {
        List<List<String>> paths = new ArrayList<>();

        this.totalPaths("start", new ArrayList<>(), paths, 1, false);

        return paths.size();
        // 5254
    }

    @Part(part = 2)
    public int secondPart() {
        List<List<String>> paths = new ArrayList<>();

        this.totalPaths("start", new ArrayList<>(), paths, 2, false);

        return paths.size();
        // 149385
    }

    public void totalPaths(String position, List<String> visited, List<List<String>> paths, int part, boolean twice) {
        visited.add(position);
        if (position.equalsIgnoreCase("end")) {
            paths.add(visited);
            return;
        }

        // Loop through every connection
        for (String neighbor : this.connections.get(position)) {
            if (part == 1) { // Part 1
                // Check if the cave is a small cave and check if we visited it already
                if (this.isStringLowerCase(neighbor) && visited.contains(neighbor)) {
                    continue;
                }
                // Loop through the method again with a copy of our visited array
                this.totalPaths(neighbor, new ArrayList<>(visited), paths, part, twice);

            } else { // part 2
                // We can not go back to start
                if (neighbor.equalsIgnoreCase("start")) {
                    continue;
                }

                // Check if the cave is a small cave and check if we visited it already
                if (this.isStringLowerCase(neighbor) && visited.contains(neighbor)) {
                    // If we already visited a small cave twice, we stop
                    if (twice) {
                        continue;
                    }
                    this.totalPaths(neighbor, new ArrayList<>(visited), paths, part, true);
                } else {
                    this.totalPaths(neighbor, new ArrayList<>(visited), paths, part, twice);
                }
            }
        }
    }

    /**
     * @link https://www.javacodeexamples.com/check-if-string-is-lowercase-in-java-example/620
     */
    private boolean isStringLowerCase(String str) {
        //convert String to char array
        char[] charArray = str.toCharArray();
        for (char c : charArray) {
            //if the character is a letter
            if (Character.isLetter(c)) {
                //if any character is not in lower case, return false
                if (!Character.isLowerCase(c))
                    return false;
            }
        }
        return true;
    }
}
