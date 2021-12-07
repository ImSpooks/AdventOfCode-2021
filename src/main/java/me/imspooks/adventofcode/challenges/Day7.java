package me.imspooks.adventofcode.challenges;

import me.imspooks.adventofcode.challenges.interfaces.Day;
import me.imspooks.adventofcode.challenges.interfaces.Part;
import me.imspooks.adventofcode.util.InputReader;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class Day7 implements Day {

    private List<String> lines;

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-7/input").getTextLines(StandardCharsets.UTF_8);
    }

    @Part(part = 1)
    public int firstPart() {
        List<Integer> positions = new ArrayList<>();

        // Convert the input to an int array, although we only have 1 line to convert, this supports multi-line input.
        this.lines.forEach(string -> positions.addAll(Arrays.stream(string.split(",")).map(Integer::parseInt).collect(Collectors.toList())));

        // Get the highest available position
        Optional<Integer> optionalHighest = positions.stream().max(Comparator.comparingInt(Integer::intValue));
        if (!optionalHighest.isPresent()) {
            // Should never happen, this was made so our IDE wouldn't complain about the present check warning
            throw new NullPointerException("Highest value not found");
        }

        int highest = optionalHighest.get();

        // We kinda brute force this, since I have no clue how to get the right position without looping over everything.
        int minimum = Integer.MAX_VALUE;
        int pos = -1;

        for (int i = 0; i < highest; i++) {
            int fuel = 0;

            for (Integer position : positions) {
                fuel += Math.abs(position - i);
            }

            if (minimum > fuel) {
                minimum = fuel;
                pos = i;
            }
        }

        return minimum;
    }

    @Part(part = 2)
    public int secondPart() {
        // Function to convert the amount of steps to the desired amount of fuel
        // The first step costs 1 fuel, second step costs 2 and the third costs 3, so these 3 steps will cost 6 fuel in total
        Function<Integer, Integer> convertFuel = input -> {
            int value = 0;
            for (int i = 1; i <= input; i++) {
                value += i;
            }
            return value;
        };

        List<Integer> positions = new ArrayList<>();

        // Convert the input to an int array, although we only have 1 line to convert, this supports multi-line input.
        this.lines.forEach(string -> positions.addAll(Arrays.stream(string.split(",")).map(Integer::parseInt).collect(Collectors.toList())));

        // Get the highest available position
        Optional<Integer> optionalHighest = positions.stream().max(Comparator.comparingInt(Integer::intValue));
        if (!optionalHighest.isPresent()) {
            // Should never happen, this was made so our IDE wouldn't complain about the present check warning
            throw new NullPointerException("Highest value not found");
        }

        int highest = optionalHighest.get();

        // We kinda brute force this, since I have no clue how to get the right position without looping over everything.
        int minimum = Integer.MAX_VALUE;
        int pos = -1;

        for (int i = 0; i < highest; i++) {
            int fuel = 0;

            for (Integer position : positions) {
                fuel += convertFuel.apply(Math.abs(position - i));
            }

            if (minimum > fuel) {
                minimum = fuel;
                pos = i;
            }
        }

        return minimum;
    }
}
