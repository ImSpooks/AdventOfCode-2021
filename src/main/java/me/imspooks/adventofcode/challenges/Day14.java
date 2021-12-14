package me.imspooks.adventofcode.challenges;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.imspooks.adventofcode.challenges.interfaces.Day;
import me.imspooks.adventofcode.challenges.interfaces.Part;
import me.imspooks.adventofcode.util.InputReader;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class Day14 implements Day {

    private static final Pattern REGEX = Pattern.compile("^([A-Z]{2}) -> ([A-Z])$");

    private String input;
    private List<String> lines;

    private List<Insertion> insertions = new ArrayList<>();

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-14/input").getTextLines(StandardCharsets.UTF_8);
        this.input = this.lines.get(0);

        // Skip the first 2 lines since they don't matter
        for (int i = 2; i < this.lines.size(); i++) {
            final Matcher matcher = REGEX.matcher(this.lines.get(i));

            if (matcher.find()) {
                this.insertions.add(new Insertion(matcher.group(1), matcher.group(2).charAt(0)));
            }
        }
    }

    private Insertion getInsertion(String pattern) {
        for (Insertion insertion : this.insertions) {
            if (insertion.getPattern().equalsIgnoreCase(pattern)) {
                return insertion;
            }
        }
        return null;
    }

    @Part(part = 1)
    public long firstPart() {
        return this.getResult(10);
    }

    @Part(part = 2)
    public long secondPart() {
        return this.getResult(40);
    }

    private long getResult(int steps) {
        String input = this.input;
        final Map<String, Long> pairs = new HashMap<>();
        final Map<Character, Long> letters = new HashMap<>();

        for (int i = 0; i < input.length() - 1; i++) {
            String pair = input.substring(i, i + 2);
            // Put each current pair in our list
            pairs.put(pair, pairs.getOrDefault(pair, 0L) + 1L);
        }

        // Set the amount of letters we have
        for (char c : input.toCharArray()) {
            letters.put(c, letters.getOrDefault(c, 0L) + 1);
        }

        for (int i = 0; i < steps; i++) {
            final Map<String, Long> newPairs = new HashMap<>();

            // Loop through the current pairs that we have
            for (Map.Entry<String, Long> entry : pairs.entrySet()) {
                String pair = entry.getKey();
                long value = entry.getValue();

                // Get the insertion for our pair
                char insertion = Objects.requireNonNull(this.getInsertion(pair), "No insertion found for pair " + pair).getCharacter();

                // Generator our new first and last pair
                String newFirstPair = String.valueOf(new char[] {pair.charAt(0), insertion});
                String newLastPair = String.valueOf(new char[] {insertion, pair.charAt(1)});

                // Add them to the new pairs map
                newPairs.put(newFirstPair, newPairs.getOrDefault(newFirstPair, 0L) + value);
                newPairs.put(newLastPair, newPairs.getOrDefault(newLastPair, 0L) + value);

                // Add our value to the current letters
                letters.put(insertion, letters.getOrDefault(insertion, 0L) + value);
            }
            // Replace our current pairs with the new ones
            pairs.clear();
            pairs.putAll(newPairs);
        }

        List<Long> result = letters.values().stream().sorted().collect(Collectors.toList());

        return result.get(result.size() - 1) - result.get(0);
    }

    /**
     * Don't use this method because it can cause an out of memory error when using a high step amount;
     */
    private long getResultBad(int steps) {
        String input = this.input;
        System.out.println("Template: " + this.input);

        for (int step = 0; step < steps; step++) {
            List<InsertedCharacters> insertedCharacters = new ArrayList<>();

            for (int i = 0; i < input.length() - 1; i++) {
                for (Insertion insertion : this.insertions) {
                    if (insertion.getPattern().equalsIgnoreCase(String.valueOf(new char[] {input.charAt(i), input.charAt(i + 1)}))) {
                        insertedCharacters.add(new InsertedCharacters(i + 1 + insertedCharacters.size(), insertion.getCharacter()));
                    }
                }
            }

            StringBuilder builder = new StringBuilder(input);

            for (InsertedCharacters insertedCharacter : insertedCharacters) {
                builder.insert((int) insertedCharacter.getPosition(), insertedCharacter.getCharacter());
            }

            input = builder.toString();

            System.out.println("After step " + (step + 1) + ": (" + input.length() + ") "/* + input*/);
        }

        Map<Character, Long> characterAmount = new HashMap<>();

        for (char c : input.toCharArray()) {
            characterAmount.put(c, characterAmount.getOrDefault(c, 0L) + 1);
        }

        long highest = characterAmount.values().stream().max(Long::compareTo).orElse(0L);
        long lowest = characterAmount.values().stream().min(Long::compareTo).orElse(0L);

        return highest - lowest;
    }

    @Getter
    @RequiredArgsConstructor
    @ToString
    private static class Insertion {
        private final String pattern;
        private final char character;
    }

    @Getter
    @RequiredArgsConstructor
    @ToString
    @Deprecated
    private static class InsertedCharacters {
        private final long position;
        private final char character;
    }
}
