package me.imspooks.adventofcode.challenges;

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
public class Day8 implements Day {

    private static final Pattern pattern = Pattern.compile("^(\\D*) \\| (\\D*)$", Pattern.MULTILINE);

    private List<String> lines;

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-8/input").getTextLines(StandardCharsets.UTF_8);
    }

    @Part(part = 1)
    public int firstPart() {
        int total = 0;

        // Loop trough each line and see if it goes through our regex
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                // Split the results
                String[] results = matcher.group(2).split(" ");

                // Check if the result is 1, 4, 7 or 8 via length
                for (String result : results) {
                    // 1, 7, 4, 8
                    if (result.length() == 2 || result.length() == 3 || result.length() == 4 || result.length() == 7) {
                        total++;
                    }
                }
            }
        }

        return total;
    }

    @Part(part = 2)
    public int secondPart() {
        // List where we store each result
        List<Integer> results = new ArrayList<>();

        // Loop trough each line and see if it goes through our regex
        for (int i = 0; i < this.lines.size(); i++) {
            final String line = this.lines.get(i);

            final Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                // Keep track what characters make up which digit
                final Map<Integer, String> numbers = new HashMap<>();

                // Put 8 in because we already know that one
                numbers.put(8, "abcdefg");

                // Split the results before the delimiter '|', sort from the lowest amount to the highest amount of characters in alphabetical order;
                final List<String> before = Arrays.stream(matcher.group(1).split(" ")).sorted(Comparator.comparingInt(String::length)).map(this::sortString).collect(Collectors.toList());



                for (String s : before) {
                    switch (s.length()) {
                        case 2: { // 1
                            numbers.put(1, s);
                            break;
                        }
                        case 3: { // 7
                            numbers.put(7, s);
                            break;
                        }
                        case 4: { // 4
                            numbers.put(4, s);
                            break;
                        }

                        case 5: { // 2, 3, 5,
                            // Matches the 1 pattern
                            if (Arrays.stream(numbers.get(1).split("")).allMatch(s::contains)) {
                                numbers.put(3, s);
                            }
                            // Matches the reversion of pattern 4
                            else if (Arrays.stream(numbers.get(8).split("")).filter(c -> !numbers.get(4).contains(c)).allMatch(s::contains)) {
                                numbers.put(2, s);
                            }
                            // 5 otherwise
                            else {
                                numbers.put(5, s);
                            }
                            break;
                        }

                        case 6: { // 0, 6, 9
                            // Matches the 4 pattern
                            if (Arrays.stream(numbers.get(4).split("")).allMatch(s::contains)) {
                                numbers.put(9, s);
                            }
                            else if (Arrays.stream(numbers.get(7).split("")).allMatch(s::contains)) {
                                numbers.put(0, s);
                            }
                            // Only 6 is left
                            else {
                                numbers.put(6, s);
                            }
                            break;
                        }
                    }
                }

                // Now we check for the output, we do the exact same as above, but instead we do not sort the list on length
                final List<String> after = Arrays.stream(matcher.group(2).split(" ")).map(this::sortString).collect(Collectors.toList());

                final StringBuilder result = new StringBuilder();

                after.removeIf(s -> {
                    for (Map.Entry<Integer, String> entry : numbers.entrySet()) {
                        if (s.equalsIgnoreCase(entry.getValue())) {
                            result.append(entry.getKey());
                            return true;
                        }
                    }

                    return false;
                });

                if (!after.isEmpty()) {
                    throw new RuntimeException("List " + (i + 1) + " is not empty: " + after + ", numbers=" + numbers);
                }

                results.add(Integer.parseInt(result.toString()));
            }
        }

        return results.stream().reduce(0, Integer::sum);
    }


    // https://www.geeksforgeeks.org/sort-a-string-in-java-2-different-ways/
    // To sort a string alphabetically
    private String sortString(String inputString) {
        // Converting input string to character array
        char[] tempArray = inputString.toCharArray();

        // Sorting temp array using
        Arrays.sort(tempArray);

        // Returning new sorted string
        return new String(tempArray);
    }
}
