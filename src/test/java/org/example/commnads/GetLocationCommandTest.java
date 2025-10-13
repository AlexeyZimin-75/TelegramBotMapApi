package org.example.commnads;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetLocationCommandTest {
    Command command = new GetLocationCommand();

    @Test
    void getCommandName() {
        assertEquals("location",command.getCommandName());
    }

    @Test
    void getDescription() {
        assertEquals("Геолокация",command.getDescription());
    }

    @Test
    void execute() {

    }

    @Test
    void showDataOfLocation() {
    }

    @Test
    void getCityFromCoordinates() {
    }
}