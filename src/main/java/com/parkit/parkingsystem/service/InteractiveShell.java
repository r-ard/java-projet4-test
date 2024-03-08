package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class InteractiveShell {

    private static final Logger logger = LogManager.getLogger("InteractiveShell");

    private InputReaderUtil inputReaderUtil;
    private ParkingSpotDAO parkingSpotDAO;

    private TicketDAO ticketDAO;

    private ParkingService parkingService;

    public InteractiveShell(
            InputReaderUtil inputReaderUtil,
            ParkingSpotDAO parkingSpotDAO,
            TicketDAO ticketDAO,
            ParkingService parkingService
    ) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
        this.parkingService = parkingService;
    }

    public InteractiveShell() {
        this.inputReaderUtil = new InputReaderUtil();
        this.parkingSpotDAO = new ParkingSpotDAO();
        this.ticketDAO = new TicketDAO();
        this.parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    }

    public void loadInterface(){
        logger.info("App initialized!!!");
        System.out.println("Welcome to Parking System!");

        boolean continueApp = true;

        while(continueApp){
            loadMenu();
            int option = this.inputReaderUtil.readSelection();
            switch(option){
                case 1: {
                    this.parkingService.processIncomingVehicle();
                    break;
                }
                case 2: {
                    this.parkingService.processExitingVehicle();
                    break;
                }
                case 3: {
                    System.out.println("Exiting from the system!");
                    continueApp = false;
                    break;
                }
                default: System.out.println("Unsupported option. Please enter a number corresponding to the provided menu");
            }
        }
    }

    private void loadMenu(){
        System.out.println("Please select an option. Simply enter the number to choose an action");
        System.out.println("1 New Vehicle Entering - Allocate Parking Space");
        System.out.println("2 Vehicle Exiting - Generate Ticket Price");
        System.out.println("3 Shutdown System");
    }

}
