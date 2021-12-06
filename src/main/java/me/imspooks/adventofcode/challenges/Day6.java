package me.imspooks.adventofcode.challenges;

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
public class Day6 implements Day {

    private List<String> lines;

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-6/input").getTextLines(StandardCharsets.UTF_8);
    }

    @Part(part = 1)
    public long firstPart() {
        return this.getFishAmount(80);
    }

    @Part(part = 2)
    public long secondPart() {
        return this.getFishAmount(256);
    }

    /**
     * Calculate how many fish there are after a certain amount of days
     *
     * @param days Days to reproduce
     * @return a {@link Long} value which indicates the amount of fish
     */
    private long getFishAmount(int days) {
        // Map(Days, Amount) to keep track of our resetStates, we use Bytes since values are between 0 and 8 anyway
        Map<Byte, Long> resetStates = new HashMap<>();

        // Convert the input to an int array, although we only have 1 line to convert, this supports multi-line input.
        this.lines.forEach(string -> Arrays.stream(string.split(",")).map(Byte::parseByte).forEach(b -> resetStates.put(b, resetStates.getOrDefault(b, 0L) + 1)));

        for (int day = 0; day < days; day++) {

            // Keep track of how many fish we have to add
            long fishToAdd = 0;

            // Create a temporary map to store in the new states
            Map<Byte, Long> tempMap = new HashMap<>();

            for (Map.Entry<Byte, Long> entry : resetStates.entrySet()) {
                byte state = entry.getKey();
                long amount = entry.getValue();

                // Remove a day
                state--;

                if (state < 0) {
                    fishToAdd += amount;
                    state = 6;
                }

                // Add the value to the current, e.g. if a 0 fish gets transferred to 6, and a 7 fish gets transferred to 6
                tempMap.put(state, amount + tempMap.getOrDefault(state, 0L));
            }

            if (fishToAdd > 0) {
                tempMap.put((byte) 0x8, fishToAdd);
            }

            // Clear and update
            resetStates.clear();
            resetStates.putAll(tempMap);

        }

        return resetStates.values().stream().reduce(0L, Long::sum); // 360761
    }






    /**
     * Dont use this method because it can cause a out of memory error when using a high day amount;
     */
    @Deprecated
    private long getFishAmountBad(int days) {
        // Map(Days, Amount) to keep track of our resetStates, we use Bytes since values are between 0 and 8 anyway
        List<Byte> resetStates = new ArrayList<>();

        // Convert the input to an int array, although we only have 1 line to convert, this supports multi-line input.
        this.lines.forEach(string -> resetStates.addAll(Arrays.stream(string.split(",")).map(Byte::parseByte).collect(Collectors.toList())));

        for (int day = 0; day < days; day++) {

            // Keep track of how many fish we have to add
            long fishToAdd = 0;

            for (int index = 0; index < resetStates.size(); index++) {
                // Remove one day from each reset timer and check if we can reproduce
                byte next = (byte) (resetStates.get(index) - 1);

                if (next < 0) {
                    // We can add a fish and reset our timer
                    fishToAdd++;
                    next = 0x6;
                }

                resetStates.set(index, next);
            }

            // Add the fish
            for (long i = 0; i < fishToAdd; i++) {
                resetStates.add((byte) 0x8);
            }
        }

        return resetStates.size();
    }
}
