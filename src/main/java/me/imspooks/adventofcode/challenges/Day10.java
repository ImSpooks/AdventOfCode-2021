package me.imspooks.adventofcode.challenges;

import lombok.RequiredArgsConstructor;
import me.imspooks.adventofcode.challenges.interfaces.Day;
import me.imspooks.adventofcode.challenges.interfaces.Part;
import me.imspooks.adventofcode.util.InputReader;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class Day10 implements Day {

    private List<String> lines;

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-10/input").getTextLines(StandardCharsets.UTF_8);
    }

    @Part(part = 1)
    public int firstPart() {
        // Keep track of all illegal brackets
        List<Bracket> illegalBrackets = new ArrayList<>();

        for (String line : this.lines) {
            // Keep track of our openings
            ArrayDeque<Bracket> queue = new ArrayDeque<>();

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);

                // Add the opening to our queue
                Bracket opening = Bracket.fromOpening(c);
                if (opening != null) {
                    queue.add(opening);
                    continue;
                }

                // Check if the closing matches the last opening
                Bracket closing = Bracket.fromClosing(c);
                if (closing != null) {
                    Bracket last = queue.removeLast();

                    if (last.close != closing.close) {
                        illegalBrackets.add(closing);
                    }
                }

            }
        }

        int result = 0;

        // Calculate our result -> the amount of errors * the error value
        for (Bracket bracket : Bracket.CACHE) {
            result += bracket.part1Value * illegalBrackets.stream().filter(b -> b == bracket).count();
        }

        return result;
    }

    @Part(part = 2)
    public long secondPart() {
        Map<String, ArrayDeque<Bracket>> incompleteLines = new LinkedHashMap<>();
        this.lines.forEach(line -> incompleteLines.put(line, new ArrayDeque<>()));

        // ([{{

        // Remove the illegal lines
        incompleteLines.entrySet().removeIf(entry -> {
            String line = entry.getKey();

            // Keep track of our openings
            ArrayDeque<Bracket> queue = entry.getValue();

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);

                // Add the opening to our queue
                Bracket opening = Bracket.fromOpening(c);
                if (opening != null) {
                    queue.add(opening);
                    continue;
                }

                // Check if the closing matches the last opening
                Bracket closing = Bracket.fromClosing(c);
                if (closing != null) {
                    Bracket last = queue.removeLast();

                    if (last.close != closing.close) {
                        return true;
                    }
                }
            }
            return false;
        });

        // Remove the complete lines if any
        incompleteLines.entrySet().removeIf(entry -> entry.getValue().isEmpty());

        // Now finish our incomplete lines with values
        List<Long> missingValues = new ArrayList<>();

        incompleteLines.values().forEach(queue -> {
            long result = 0;

            // Calculate our result -> result = result * 5 + value
            while (queue.peek() != null) {
                Bracket bracket = queue.removeLast(); // We 'reverse' it because the last one in the queue should end fist
                result = (result * 5) + bracket.part2Value;
            }
            missingValues.add(result);
        });

        // Sort our values
        missingValues.sort(Comparator.comparingLong(Long::longValue));

        return missingValues.get(missingValues.size() / 2);
    }

    @RequiredArgsConstructor
    private enum Bracket {
        ROUND('(', ')', 3, 1),
        SQUARE('[', ']', 57, 2),
        CURLY('{', '}', 1197, 3),
        ANGLE('<', '>', 25137, 4),
        ;

        private final char open;
        private final char close;

        private final int part1Value;
        private final int part2Value;

        public static final Bracket[] CACHE = values();

        public static Bracket getBracket(char c) {
            for (Bracket bracket : CACHE) {
                if (bracket.open == c || bracket.close == c) {
                    return bracket;
                }
            }
            return null;
        }

        public static Bracket fromOpening(char c) {
            for (Bracket bracket : CACHE) {
                if (bracket.open == c) {
                    return bracket;
                }
            }
            return null;
        }

        public static Bracket fromClosing(char c) {
            for (Bracket bracket : CACHE) {
                if (bracket.close == c) {
                    return bracket;
                }
            }
            return null;
        }
    }
}
