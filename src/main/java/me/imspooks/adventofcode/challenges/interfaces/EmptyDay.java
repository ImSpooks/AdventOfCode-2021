package me.imspooks.adventofcode.challenges.interfaces;

import me.imspooks.adventofcode.util.InputReader;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class EmptyDay implements Day {

    private List<String> lines;

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-?/input").getTextLines(StandardCharsets.UTF_8);
    }

    @Part(part = 1)
    public int firstPart() {

        return 0;
    }

    @Part(part = 2)
    public int secondPart() {

        return 0;
    }
}
