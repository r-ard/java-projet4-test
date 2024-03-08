package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
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
public class InteractiveShellTest {
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    @Mock
    private static InputReaderUtil inputReaderUtil;

    @Mock
    private static TicketDAO ticketDAO;

    @Mock
    private static ParkingSpotDAO parkingSpotDAO;

    private static ParkingService parkingService;

    @BeforeEach
    public void setUpEach() {
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;

        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    }

    private void setupInteractiveShellTest(
            int actionChoice,
            int vehiculeType
    ) {
        when(inputReaderUtil.readSelection()).thenReturn(
                actionChoice,
                vehiculeType,
                3 // Exit app choice
        );

        // Incomming hooks
        if(actionChoice == 1) {
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
            when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        }

        // Exiting hooks
        if(actionChoice == 2) {
            //when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            Ticket exitingTicket = new Ticket();
            exitingTicket.setInTime(new Date());
            exitingTicket.setParkingSpot(
                    new ParkingSpot(
                            1,
                            vehiculeType == 1 ? ParkingType.CAR : ParkingType.BIKE,
                            false
                    )
            );
            exitingTicket.setVehicleRegNumber("ABCDEFGH");
            exitingTicket.setPrice(0);
            exitingTicket.setId(1);

            when(ticketDAO.getTicket(anyString())).thenReturn(exitingTicket);
        }

        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEFGH");
        }
        catch (Exception e) {}
    }

    @Test
    public void processIncommingCarTest() {
        this.setupInteractiveShellTest(
                1,
                1
        );

        InteractiveShell interactiveShell = new InteractiveShell(
                inputReaderUtil,
                parkingSpotDAO,
                ticketDAO,
                parkingService
        );

        interactiveShell.loadInterface();

        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    public void processIncommingBikeTest() {
        this.setupInteractiveShellTest(
                1,
                2
        );


        InteractiveShell interactiveShell = new InteractiveShell(
                inputReaderUtil,
                parkingSpotDAO,
                ticketDAO,
                parkingService
        );

        interactiveShell.loadInterface();

        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    public void processExitingCarTest() {
        this.setupInteractiveShellTest(
                2,
                1
        );

        when(ticketDAO.getTicketCount(anyString())).thenReturn(0);

        InteractiveShell interactiveShell = new InteractiveShell(
                inputReaderUtil,
                parkingSpotDAO,
                ticketDAO,
                parkingService
        );

        interactiveShell.loadInterface();

        verify(ticketDAO, times(1)).updateTicket(any(Ticket.class));
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));

        assertTrue(ticketDAO.getTicket("").getParkingSpot().isAvailable());
    }
}
