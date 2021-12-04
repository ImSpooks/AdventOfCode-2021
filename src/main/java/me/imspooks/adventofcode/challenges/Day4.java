package me.imspooks.adventofcode.challenges;

import me.imspooks.adventofcode.challenges.interfaces.Day;
import me.imspooks.adventofcode.challenges.interfaces.Part;
import me.imspooks.adventofcode.util.InputReader;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class Day4 implements Day {

    private List<String> lines;

    @Override
    public void preRun() {
        this.lines = InputReader.toReader("/assets/day-4/input").getTextLines(StandardCharsets.UTF_8);
    }

    @Part(part = 1)
    public int firstPart() {
        // Get the daw order
        List<Integer> drawOrder = Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());

        // Get how many cards there are, each card has 5 text lines and an empty space between them.
        int cardAmount = (this.lines.size() - 2) / 6;

        List<BingoCard> cards = new ArrayList<>();

        // Keep track of our position
        int index = 2;

        for (int i = 0; i < cardAmount; i++) {
            BingoCard card = new BingoCard(i, 5);

            // Each card has 5 rows
            for (int j = 0; j < 5; j++) {
                String line = this.lines.get(index++);

                // Each card has 5 columns
                for (int k = 0; k < 5; k++) {
                    card.numbers[j][k] = Integer.parseInt(line.substring(k * 3, k * 3 + 2).replace(" ", ""));
                }
            }

            // Skip the next white line
            index++;
            cards.add(card);
        }

        for (Integer draw : drawOrder) {
            for (BingoCard card : cards) {
                // Update each card
                card.update(draw);

                // Check if the card has bingo and calculate the sum of unused numbers with the current draw
                if (card.hasBingo()) {
                    System.out.println("Card " + card.id + " has won!");
                    System.out.println("Sum of unused: " + card.sumOfUnused());
                    System.out.println("Draw: " + draw);

                    return card.sumOfUnused() * draw;
                }
            }
        }

        return 0;
    }

    @Part(part = 2)
    public int secondPart() {
        // Do the same as the first part
        List<Integer> drawOrder = Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());

        int cardAmount = (this.lines.size() - 2) / 6;

        List<BingoCard> cards = new ArrayList<>();

        int index = 2;

        for (int i = 0; i < cardAmount; i++) {
            BingoCard card = new BingoCard(i, 5);

            for (int j = 0; j < 5; j++) {
                String line = this.lines.get(index++);

                for (int k = 0; k < 5; k++) {
                    card.numbers[j][k] = Integer.parseInt(line.substring(k * 3, k * 3 + 2).replace(" ", ""));
                }
            }

            index++;
            cards.add(card);
        }

        for (Integer draw : drawOrder) {
            // Keep track of the last card;
            if (cards.size() == 1) {
                BingoCard card = cards.get(0);
                card.update(draw);

                if (card.hasBingo()) {
                    System.out.println("Card " + card.id + " got last place!");
                    System.out.println("Sum of unused: " + card.sumOfUnused());
                    System.out.println("Draw: " + draw);

                    return card.sumOfUnused() * draw;
                }
                continue;
            }

            // Remove if the card has bingo
            cards.removeIf(card -> {
                card.update(draw);
                return card.hasBingo();
            });
        }

        return 0;
    }

    private class BingoCard {
        private final int id;
        private final int[][] numbers;
        private final boolean[][] marked;

        private BingoCard(int id, int length) {
            this.id = id;
            this.numbers = new int[length][length];
            this.marked = new boolean[length][length];
        }

        public void update(int draw) {
            for (int i = 0; i < this.numbers.length; i++) {
                for (int j = 0; j < this.numbers[i].length; j++) {
                    if (this.numbers[i][j] == draw) {
                        this.marked[i][j] = true;
                    }
                }
            }
        }

        public boolean hasBingo() {
            // Horizontal
            for (int row = 0; row < numbers.length; row++) {
                boolean rowIsABingo = true;
                for (int col = 0; col < numbers[row].length; col++) {
                    // We have to check that everything is marked
                    rowIsABingo = rowIsABingo && this.marked[row][col];
                }
                if (rowIsABingo) {
                    return true;
                }
            }

            // Horizontal
            for (int col = 0; col < numbers.length; col++) {
                boolean rowIsABingo = true;
                for (int row = 0; row < numbers[col].length; row++) {
                    // We have to check that everything is marked
                    rowIsABingo = rowIsABingo && this.marked[row][col];
                }
                if (rowIsABingo) {
                    return true;
                }
            }

            return false; // If we didn't find a bingo, return false.
        }

        public int sumOfUnused() {
            int sum = 0;
            for (int i = 0; i < this.numbers.length; i++) {
                for (int j = 0; j < this.numbers[i].length; j++) {
                    if (!this.marked[i][j]) {
                        sum += this.numbers[i][j];
                    }
                }
            }
            return sum;
        }
    }
}