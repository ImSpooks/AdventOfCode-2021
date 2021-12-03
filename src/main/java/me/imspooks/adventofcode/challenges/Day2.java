package me.imspooks.adventofcode.challenges;

import me.imspooks.adventofcode.challenges.interfaces.Day;
import me.imspooks.adventofcode.challenges.interfaces.Part;
import me.imspooks.adventofcode.util.InputReader;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class Day2 implements Day {

    private List<String> lines;

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-2/input.txt").getTextLines(StandardCharsets.UTF_8);
    }

    @Part(part = 1)
    public int firstPart() {
        // Keep track of our position
        final AtomicInteger horizontal = new AtomicInteger(0);
        final AtomicInteger depth = new AtomicInteger(0);

        // Loop through each line
        this.lines.forEach(line -> {
            final String[] split = line.split(" ");
            final int value = Integer.parseInt(split[1]);

            // Update our position
            switch (split[0].toLowerCase(Locale.ROOT)) {
                case "forward": {
                    horizontal.set(horizontal.get() + value);
                    break;
                }

                case "up": {
                    depth.set(depth.get() - value);
                    break;
                }

                case "down": {
                    depth.set(depth.get() + value);
                    break;
                }
            }
        });

        System.out.println("Horizontal: " + horizontal.get());
        System.out.println("Depth: " + depth.get());
        return horizontal.get() * depth.get();
    }

    @Part(part = 2)
    public int secondPart() {
        // Keep track of our position
        final AtomicInteger horizontal = new AtomicInteger(0);
        final AtomicInteger depth = new AtomicInteger(0);
        final AtomicInteger aim = new AtomicInteger(0);

        // Loop through each line
        this.lines.forEach(line -> {
            final String[] split = line.split(" ");
            final int value = Integer.parseInt(split[1]);

            // Update our position
            switch (split[0].toLowerCase(Locale.ROOT)) {
                case "forward": {
                    horizontal.set(horizontal.get() + value);
                    depth.set(depth.get() + (aim.get() * value));
                    break;
                }

                case "up": {
                    aim.set(aim.get() - value);
                    break;
                }

                case "down": {
                    aim.set(aim.get() + value);
                    break;
                }
            }
        });

        System.out.println("Horizontal: " + horizontal.get());
        System.out.println("Depth: " + depth.get());
        return horizontal.get() * depth.get();
    }
}