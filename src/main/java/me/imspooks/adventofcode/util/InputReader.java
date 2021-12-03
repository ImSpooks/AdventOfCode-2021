package me.imspooks.adventofcode.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class InputReader {

    private final String resource;
    private final InputStream inputStream;

    private InputReader(String resource) {
        this.resource = resource;
        this.inputStream = this.getClass().getResourceAsStream(resource);
    }

    public List<String> getTextLines(Charset charset) {
        return new BufferedReader(new InputStreamReader(this.inputStream, charset)).lines().collect(Collectors.toList());
    }

    public Stream<String> getLines(Charset charset) {
        return new BufferedReader(new InputStreamReader(this.inputStream, charset)).lines();
    }

    public static InputReader toReader(String resource) {
        return new InputReader(resource);
    }
}