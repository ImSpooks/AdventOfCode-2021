package me.imspooks.adventofcode.day2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Nick on 03 dec. 2021.
 * Copyright © ImSpooks
 */
public class DiveB {

    public static void main(String[] args) {
        // Get our file as an input stream
        final InputStream inputStream = DiveB.class.getResourceAsStream("/input.txt");

        // Create a new reader and make sure our input stream is not null
        assert inputStream != null;
        final Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        try (final BufferedReader bufferedReader = new BufferedReader(reader)) {

            // Keep track of our position
            final AtomicInteger horizontal = new AtomicInteger(0);
            final AtomicInteger depth = new AtomicInteger(0);
            final AtomicInteger aim = new AtomicInteger(0);

            // Loop through each line
            bufferedReader.lines().forEach(line -> {
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

            System.out.println("horizontal = " + horizontal.get());
            System.out.println("depth = " + depth.get());
            System.out.println("result = " + (horizontal.get() * depth.get()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}