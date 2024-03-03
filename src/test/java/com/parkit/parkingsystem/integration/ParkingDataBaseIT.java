package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    @Mock
    private static ParkingSpotDAO parkingSpotDAO;

    @Mock
    private static TicketDAO ticketDAO;

    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    public static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

        Ticket ticket = new Ticket();

        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setInTime(new Date());
        ticket.setParkingSpot(
                new ParkingSpot(1, ParkingType.CAR, false)
        );

        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        String registrationNumber = null;
        try {
            registrationNumber = inputReaderUtil.readVehicleRegistrationNumber();
        }
        catch(Exception ex) { }

        assertNotEquals(registrationNumber, null);

        Ticket registeredTicket = ticketDAO.getTicket(registrationNumber);
        assertNotEquals(registeredTicket, null);

        ParkingSpot parkingSpot = registeredTicket.getParkingSpot();
        assertNotEquals(parkingSpot, null);
        assertEquals(parkingSpot.isAvailable(), false);
    }

    @Test
    public void testParkingLotExit(){
        testParkingACar();

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();

        String registrationNumber = null;
        try {
            registrationNumber = inputReaderUtil.readVehicleRegistrationNumber();
        }
        catch(Exception ex) { }

        assertNotEquals(registrationNumber, null);

        Ticket exitingTicket = ticketDAO.getTicket(registrationNumber);

        assertNotEquals(exitingTicket, null);
        assertNotEquals(exitingTicket.getOutTime(), null);
    }

    @Test
    public void testParkingLotExitRecurringUser() {
        testParkingACar();

        when(ticketDAO.getTicketCount(anyString())).thenReturn(2);

        String registrationNumber = null;
        try {
            registrationNumber = inputReaderUtil.readVehicleRegistrationNumber();
        }
        catch(Exception ex) { }

        assertNotEquals(registrationNumber, null);

        Ticket exitingTicket = ticketDAO.getTicket(registrationNumber);
        assertNotEquals(exitingTicket, null);

        exitingTicket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000))); // Set ticket's in time to 1 hour ago

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();

        assertEquals(exitingTicket.getPrice(), Fare.CAR_RATE_PER_HOUR * 0.95D);
    }
}
