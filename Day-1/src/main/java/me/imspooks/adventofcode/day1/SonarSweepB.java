package me.imspooks.adventofcode.day1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by Nick on 03 dec. 2021.
 * Copyright © ImSpooks
 */
public class SonarSweepB {

    public static void main(String[] args) {
        // Get our file as an input stream
        final InputStream inputStream = SonarSweepA.class.getResourceAsStream("/input.txt");

        // Create a new reader and make sure our input stream is not null
        assert inputStream != null;
        final Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        try (final BufferedReader bufferedReader = new BufferedReader(reader)) {
            final AtomicInteger increaseAmount = new AtomicInteger(0);

            // Convert text file to integers
            List<Integer> values = bufferedReader.lines().parallel().map(Integer::parseInt).collect(Collectors.toList());

            // Compare the first 3 values from index with the 3 values from index + 1
            for (int i = 0; i < values.size() - 3; i++) {
                if (values.get(i) + values.get(i + 1) + values.get(i + 2)
                        < values.get(i + 1) + values.get(i + 2) + values.get(i + 3)) {
                    increaseAmount.incrementAndGet();
                }
            }

            System.out.println("increaseAmount = " + increaseAmount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}