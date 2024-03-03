package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class InputReaderUtil {
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");

    private static Scanner defaultScan = new Scanner(System.in);

    private Scanner reader;

    public InputReaderUtil() {
        this.reader = defaultScan;
    }

    public InputReaderUtil(Scanner scan) {
        this.reader = scan;
    }

    public void setScanner(Scanner scan) {
        this.reader = scan;
    }

    public String readLine() {
        return reader.nextLine();
    }

    public int readSelection() {
        try {
            int input = Integer.parseInt(readLine());
            return input;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    public String readVehicleRegistrationNumber() throws Exception {
        try {
            String vehicleRegNumber= readLine();
            if(vehicleRegNumber == null || vehicleRegNumber.trim().length()==0) {
                throw new IllegalArgumentException("Invalid input provided");
            }
            return vehicleRegNumber;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell", e);
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
    }


}
