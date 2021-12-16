package me.imspooks.adventofcode.challenges;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.imspooks.adventofcode.challenges.interfaces.Day;
import me.imspooks.adventofcode.challenges.interfaces.Part;
import me.imspooks.adventofcode.util.InputReader;
import me.imspooks.adventofcode.util.NumberConversions;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class Day16 implements Day {

    private String line;

    @Override
    public void preRun() {
        this.line = InputReader.toReader("/assets/day-16/input").getString(StandardCharsets.UTF_8);
    }

    @Part(part = 1)
    public long firstPart() {
        // Manually keep track of sum of versions
        AtomicLong versionSum = new AtomicLong(0L);

        // Convert our hex to binary
        String hex = line;
        String binary = hexToBinary(hex);

        this.readPacket(binary, new AtomicInteger(), versionSum);
        return versionSum.get();
    }

    @Part(part = 2)
    public long secondPart() {
        // Convert our hex to binary
        String hex = line;
        String binary = hexToBinary(hex);

        return this.readPacket(binary, new AtomicInteger(), new AtomicLong());
    }

    private long readPacket(String binary, AtomicInteger position, AtomicLong versionSum) {
        // Create a function, so we can use substring with our position, and because I'm are lazy
        BiFunction<String, Integer, String> substring = (str, l) -> str.substring(position.get(), position.addAndGet(l));

        // Get the packet version, return 0 if we don't need it
        String packetVersionStr = substring.apply(binary, 3);
        long packetVersion = binaryToDecimal(packetVersionStr);

        // Get the version sum, because we need it for part 1
        versionSum.addAndGet(packetVersion);

        // Get the packet type, return 4 if we don't need it
        String packetTypeIdStr = substring.apply(binary, 3);
        long packetTypeId = binaryToDecimal(packetTypeIdStr);

        if (packetTypeId != 4) {
            long lengthId = binaryToDecimal(substring.apply(binary, 1));

            List<Long> result = new ArrayList<>();

            if (lengthId == 0) {
                // Read the next 15 bits, which indicates the length of the upcoming packets
                long subPacketLength = binaryToDecimal(substring.apply(binary, 15));
                int values = NumberConversions.ceil(subPacketLength / 16.0);

                int currentPos = position.get();

                for (int i = 0; i < values; i++) {
                    result.add(this.readPacket(binary, position, versionSum));

                    if (currentPos + subPacketLength <= position.get()) {
                        break;
                    }
                }

            } else if (lengthId == 1) {
                // Read the next 11 bits, which indicates the amount of upcoming packets
                String t = substring.apply(binary, 11);
                long packets = binaryToDecimal(t);

                for (int i = 0; i < packets; i++) {
                    result.add(this.readPacket(binary, position, versionSum));
                }
            }

            return PacketType.getType(packetTypeId).getFunction().apply(result);
        } else {
            // Packet type 4 means it returns a literal value
            // No version or id needed, because we already have those
            return this.readLiteralValue(binary, position, false, false);
        }
    }

    /*
        Has to be long, since we parse numbers with bits that surpass the 32 bit mark
     */
    private long readLiteralValue(String binary, AtomicInteger position, boolean withVersion, boolean withId) {
        // Create a function, so we can use substring with our position, and because I'm are lazy
        BiFunction<String, Integer, String> substring = (str, l) -> str.substring(position.get(), position.addAndGet(l));

        // Get the packet version, return 0 if we don't need it
        String packetVersionStr = withVersion ? substring.apply(binary, 3) : "0000";
        long packetVersion = binaryToDecimal(packetVersionStr);

        // Get the packet type, return 4 if we don't need it
        String packetTypeIdStr = withId ? substring.apply(binary, 3) : "1000";
        long packetTypeId = binaryToDecimal(packetTypeIdStr);

        StringBuilder value = new StringBuilder();

        boolean closing = false;
        while (!closing) {
            String group = substring.apply(binary, 5);

            if (group.charAt(0) == '0') {
                closing = true;
            }

            value.append(group.substring(1));
        }

        return binaryToDecimal(value.toString());
    }

    private static long binaryToDecimal(String binary) {
        return Long.parseLong(binary, 2);
    }

    private static long hexToDecimal(String binary) {
        return Long.parseLong(binary, 16);
    }

    private static String binaryToHex(String binary) {
        return Long.toString(binaryToDecimal(binary), 16);
    }

    private static String hexToBinary(String hex) {
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < hex.length(); i++) {
            StringBuilder tempBinary = new StringBuilder();
            tempBinary.append(Long.toString(hexToDecimal(hex.substring(i, i + 1)), 2));

            // Add missing 0's because it is a hex is 4 bits long: E = 1110, F = 1111
            while (tempBinary.length() < 4) {
                tempBinary.insert(0, 0);
            }
            binary.append(tempBinary);
        }

        return binary.toString();
    }

    @Getter
    @RequiredArgsConstructor
    private enum PacketType {
        SUM(0, list -> list.stream().reduce(0L, Long::sum)),
        PRODUCT(1, list -> list.stream().reduce(1L, (a, b) -> a * b)),
        MINIMUM(2, list -> list.stream().min(Comparator.comparingLong(Long::longValue)).orElseThrow(() -> new NoSuchElementException("No value present"))),
        MAXIMUM(3, list -> list.stream().max(Comparator.comparingLong(Long::longValue)).orElseThrow(() -> new NoSuchElementException("No value present"))),
        LITERAL(4, list -> list.get(0)),

        // Always have 2 values
        GREATER_THAN(5, list -> list.get(0) > list.get(1) ? 1L : 0L),
        LESS_THAN(6, list -> list.get(0) < list.get(1) ? 1L : 0L),
        EQUALS(7, list -> Objects.equals(list.get(0), list.get(1)) ? 1L : 0L),
        ;

        public static final PacketType[] CACHE = values();

        private final int id;
        private final Function<List<Long>, Long> function;

        public static PacketType getType(int id) {
            for (PacketType packetType : CACHE) {
                if (packetType.id == id) {
                    return packetType;
                }
            }
            throw new IllegalArgumentException("Packet with id \"" + id + "\" not found");
        }

        public static PacketType getType(long id) {
            for (PacketType packetType : CACHE) {
                if (packetType.id == id) {
                    return packetType;
                }
            }
            throw new IllegalArgumentException("Packet with id \"" + id + "\" not found");
        }
    }
}
