package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeWorkTest {

    HomeWork homeWork = new HomeWork();

    @Test
    void check() {
        HomeWork hw = new HomeWork();
        assertEquals(-1.0, hw.calculate("1 + 2 * ( 3 - 4 )"), 0.0001);
    }

}