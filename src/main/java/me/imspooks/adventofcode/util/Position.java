package me.imspooks.adventofcode.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Created by Nick on 05 Dec 2021.
 * Copyright © ImSpooks
 */
@Getter
@Setter
@AllArgsConstructor
public class Position<T> {
    private T x;
    private T y;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position<?> position = (Position<?>) o;
        return Objects.equals(x, position.x) && Objects.equals(y, position.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}