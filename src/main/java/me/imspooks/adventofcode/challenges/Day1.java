package me.imspooks.adventofcode.challenges;

import me.imspooks.adventofcode.challenges.interfaces.Day;
import me.imspooks.adventofcode.challenges.interfaces.Part;
import me.imspooks.adventofcode.util.InputReader;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class Day1 implements Day {

    private List<String> lines;

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-1/input.txt").getTextLines(StandardCharsets.UTF_8);
    }

    @Part(part = 1)
    public int firstPart() {
        final AtomicInteger increaseAmount = new AtomicInteger(-1);
        final AtomicInteger lastMeasurement = new AtomicInteger(-1);

        // Loop through each line
        this.lines.forEach(line -> {
            // Parse line to a number
            int parsed = Integer.parseInt(line);

            if (parsed > lastMeasurement.get()) {
                increaseAmount.incrementAndGet();
            }

            // Update last measurement
            lastMeasurement.set(parsed);
        });

        return increaseAmount.get();
    }

    @Part(part = 2)
    public int secondPart() {
        final AtomicInteger increaseAmount = new AtomicInteger(0);

        // Convert text file to integers
        List<Integer> values = this.lines.stream().map(Integer::parseInt).collect(Collectors.toList());

        // Compare the first 3 values from index with the 3 values from index + 1
        for (int i = 0; i < values.size() - 3; i++) {
            if (values.get(i) + values.get(i + 1) + values.get(i + 2)
                    < values.get(i + 1) + values.get(i + 2) + values.get(i + 3)) {
                increaseAmount.incrementAndGet();
            }
        }

        return increaseAmount.get();
    }
}