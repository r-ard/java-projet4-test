package com.parkit.parkingsystem;

import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class InputReaderUtilTest {

    private static InputReaderUtil inputUtil;

    @BeforeAll
    public static void setUp() {
        inputUtil = new InputReaderUtil();
    }

    private void setScannerInput(String input) {
        inputUtil.setScanner(
                new Scanner( new ByteArrayInputStream(input.getBytes()) )
        );
    }

    @Test
    public void readSelectionNaNTest() {
        setScannerInput("test");

        int result = inputUtil.readSelection();

        assertEquals(result, -1);
    }

    @Test
    public void readSelectionValidTest() {
        setScannerInput("10");

        int result = inputUtil.readSelection();

        assertEquals(result, 10);
    }

    @Test
    public void readVehicleRegistrationNumberNullTest() {
        setScannerInput("");

        assertThrows(Exception.class, () -> {
            String result = inputUtil.readVehicleRegistrationNumber();
        });
    }

    @Test
    public void readVehicleRegistrationNumberEmptyTest() {
        setScannerInput("    ");

        assertThrows(Exception.class, () -> {
            String result = inputUtil.readVehicleRegistrationNumber();
        });
    }

    @Test
    public void readVehicleRegistrationNumberValidTest() {
        setScannerInput("ABCDEFGH");

        String result = null;

        try {
            result = inputUtil.readVehicleRegistrationNumber();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        assertNotEquals(result, null);
        assertEquals(result, "ABCDEFGH");
    }
}
