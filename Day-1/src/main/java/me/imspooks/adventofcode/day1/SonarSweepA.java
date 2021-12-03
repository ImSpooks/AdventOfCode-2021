package me.imspooks.adventofcode.day1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Nick on 03 dec. 2021.
 * Copyright © ImSpooks
 */
public class SonarSweepA {

    public static void main(String[] args) {
        // Get our file as an input stream
        final InputStream inputStream = SonarSweepA.class.getResourceAsStream("/input.txt");

        // Create a new reader and make sure our input stream is not null
        assert inputStream != null;
        final Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        try (final BufferedReader bufferedReader = new BufferedReader(reader)) {
            // We start at -1, because the first line will always be higher thus making it 0
            final AtomicInteger increaseAmount = new AtomicInteger(-1);
            final AtomicInteger lastMeasurement = new AtomicInteger(-1);

            // Loop through each line
            bufferedReader.lines().parallel().forEach(line -> {
                // Parse line to a number
                int parsed = Integer.parseInt(line);

                if (parsed > lastMeasurement.get()) {
                    increaseAmount.incrementAndGet();
                }

                // Update last measurement
                lastMeasurement.set(parsed);
            });

            System.out.println("increaseAmount = " + increaseAmount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}