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
public class Day3 implements Day {

    private List<String> lines;

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-3/input.txt").getTextLines(StandardCharsets.UTF_8);
    }

    @Part(part = 1)
    public int firstPart() {
        final int length = this.lines.get(0).length();

        // Keep track how many 1s are in a row
        Map<Integer, Integer> oneCount = new HashMap<>();

        for (int i = 0; i < length; i++) {
            final int index = i;
            oneCount.put(index, (int) this.lines.stream().filter(s -> s.charAt(index) == '1').count());
        }

        StringBuilder gammaString = new StringBuilder();
        StringBuilder epsilonString = new StringBuilder();

        // Append the bit to each string
        for (int i = 0; i < length; i++) {
            if (oneCount.get(i) > this.lines.size() / 2) {
                gammaString.append("1");
                epsilonString.append("0");
            } else {
                gammaString.append("0");
                epsilonString.append("1");
            }
        }

        // Convert the binary number to an integer
        int gamma = Integer.parseInt(gammaString.toString(), 2);
        int epsilon = Integer.parseInt(epsilonString.toString(), 2);

        return gamma * epsilon;
    }

    @Part(part = 2)
    public int secondPart() {
        final int length = this.lines.get(0).length();

        List<String> oxygenList = new ArrayList<>(this.lines);
        List<String> co2List = new ArrayList<>(this.lines);

        // Loop trough each bit position
        for (int i = 0; i < length; i++) {
            final int index = i;

            // Get the size of each list
            int oxygenSize = oxygenList.size();
            int co2Size = co2List.size();

            if (oxygenSize > 1) {
                // Check the amount of 1's and 0's we have
                int ones = (int) oxygenList.stream().filter(line -> line.charAt(index) == '1').count();
                int zeros = oxygenSize - ones;

                // Remove each entry where the position does not match the most common bit
                oxygenList.removeIf(line -> line.charAt(index) != (ones >= zeros ? '1' : '0'));
            }

            // Repeat the process above but for co2, and use the least common bit instead of the most common one
            if (co2Size > 1) {
                int ones = (int) co2List.stream().filter(line -> line.charAt(index) == '1').count();
                int zeros = co2Size - ones;

                co2List.removeIf(line -> line.charAt(index) != (ones >= zeros ? '0' : '1'));
            }
        }

        // Convert the binary number to an integer
        int oxygen = Integer.parseInt(oxygenList.get(0), 2);
        int co2 = Integer.parseInt(co2List.get(0), 2);

        System.out.println("oxygen = " + oxygen);
        System.out.println("co2 = " + co2);

        return oxygen * co2;
    }
}