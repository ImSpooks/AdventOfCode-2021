package me.imspooks.adventofcode;

import me.imspooks.adventofcode.challenges.interfaces.Day;
import me.imspooks.adventofcode.challenges.interfaces.Part;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

/**
 * Created by Nick on 03 Dec 2021.
 * Copyright © ImSpooks
 */
public class AdventOfCode {

    public AdventOfCode(String[] args) {
        int day;
        int part;

        if (args.length > 0) {
            day = Integer.parseInt(args[0]);
            part = args.length > 1 ? Integer.parseInt(args[1]) : 0;
        } else {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter a day: ");
            day = Integer.parseInt(scanner.next());
            System.out.println("Enter a part: ");
            part = Integer.parseInt(scanner.next());
        }

        try {
            this.runChallenge(day, part);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void runChallenge(int dayNr, int partNr) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = Class.forName("me.imspooks.adventofcode.challenges.Day" + dayNr);

        if (!Day.class.isAssignableFrom(clazz)) {
            return;
        }

        Day day = (Day) clazz.getConstructor().newInstance();
        day.preRun();

        Object result = null;

        for (Method method : clazz.getMethods()) {
            Part part = method.getAnnotation(Part.class);
            if (part != null && part.part() == partNr) {
                result = method.invoke(day);
            }
        }

        System.out.println("Result for day " + dayNr + " part " + partNr + ":");
        System.out.println(result);
    }

    public static void main(String[] args) {
        new AdventOfCode(args);
    }
}