package me.imspooks.adventofcode.challenges;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.imspooks.adventofcode.challenges.interfaces.Day;
import me.imspooks.adventofcode.challenges.interfaces.Part;
import me.imspooks.adventofcode.util.InputReader;
import me.imspooks.adventofcode.util.Position;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class Day13 implements Day {

    private List<String> lines;

    private final List<Position<Integer>> positions = new ArrayList<>();
    private final List<FoldInstruction> instructions = new ArrayList<>();

    private boolean[][] map;

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-13/input").getTextLines(StandardCharsets.UTF_8);

        boolean instructions = false;
        for (String string : this.lines) {
            if (string.isEmpty()) {
                instructions = true;
                continue;
            }

            if (instructions) {
                String[] split = string.split("=");

                this.instructions.add(new FoldInstruction(split[0].contains("y"), Integer.parseInt(split[1])));
            } else {
                String[] split = string.split(",");
                this.positions.add(new Position<>(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
            }

        }

        // Map:
        //  x x x
        // y
        // y
        // y

        int width = positions.stream().map(Position::getX).max(Integer::compareTo).orElse(10) + 1;
        int height = positions.stream().map(Position::getY).max(Integer::compareTo).orElse(14) + 1;

        this.map = new boolean[width][height];

        for (Position<Integer> position : positions) {
            this.map[position.getX()][position.getY()] = true;
        }
    }

    @Part(part = 1)
    public int firstPart() {
        // Create a clone of our positions
        final List<Position<Integer>> positions = new ArrayList<>(this.positions);
        
        for (FoldInstruction instruction : this.instructions) {
            if (instruction.isHorizontal()) { // Y axis
                for (Position<Integer> position : positions) {
                    if (position.getY() > instruction.getPosition()) {
                        // Set our "old" value to false
                        this.map[position.getX()][position.getY()] = false;

                        int newPos = (position.getY() - instruction.getPosition()) * -1 + instruction.getPosition();

                        this.map[position.getX()][newPos] = true;
                    }
                }
            } else { // X axis
                for (Position<Integer> position : positions) {
                    if (position.getX() > instruction.getPosition()) {
                        // Set our "old" value to false
                        this.map[position.getX()][position.getY()] = false;

                        int newPos = (position.getX() - instruction.getPosition()) * -1 + instruction.getPosition();

                        this.map[newPos][position.getY()] = true;
                    }
                }
            }
            // Only fold the first one
            break;
        }

        // Calculate every point
        int points = 0;

        // Couldn't find a way in lambda to do this
        for (boolean[] booleans : this.map) {
            for (boolean aBoolean : booleans) {
                if (aBoolean) {
                    points++;
                }
            }
        }

        return points;
    }

    @Part(part = 2)
    public int secondPart() {
        // Create a clone of our positions
        final List<Position<Integer>> positions = new ArrayList<>(this.positions);

        for (FoldInstruction instruction : this.instructions) {
            if (instruction.isHorizontal()) { // Y axis
                for (Position<Integer> position : positions) {
                    if (position.getY() > instruction.getPosition()) {
                        // Set our "old" value to false
                        this.map[position.getX()][position.getY()] = false;

                        int newPos = (position.getY() - instruction.getPosition()) * -1 + instruction.getPosition();

                        this.map[position.getX()][newPos] = true;
                    }
                }
            } else { // X axis
                for (Position<Integer> position : positions) {
                    if (position.getX() > instruction.getPosition()) {
                        // Set our "old" value to false
                        this.map[position.getX()][position.getY()] = false;

                        int newPos = (position.getX() - instruction.getPosition()) * -1 + instruction.getPosition();

                        this.map[newPos][position.getY()] = true;
                    }
                }
            }
            // Get our new positions
            positions.clear();
            for (int x = 0; x < this.map.length; x++) {
                for (int y = 0; y < this.map[x].length; y++) {
                    if (this.map[x][y]) {
                        positions.add(new Position<>(x, y));
                    }
                }
            }
        }


        // Get the new width and height of our output
        int width = 0;
        int height = 0;

        for (int x = 0; x < this.map.length; x++) {
            for (int y = 0; y < this.map[x].length; y++) {
                if (this.map[x][y]) {
                    width = Math.max(width, x);
                    height = Math.max(height, y);
                }
            }
        }

        // Finally, generate our string
        for (int y = 0; y <= height; y++) {
            StringBuilder builder = new StringBuilder();
            for (int x = 0; x <= width; x++) {
                if (positions.contains(new Position<>(x, y))) {
                    builder.append("█");
                } else {
                    builder.append(".");
                }
            }
            System.out.println(builder);
        }

        return 0;
    }

    @Getter
    @RequiredArgsConstructor
    private static class FoldInstruction {
        private final boolean horizontal;
        private final int position;
    }
}
