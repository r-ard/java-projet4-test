package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Date;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DAODatabaseFailTest {
    @Mock
    private static DataBaseTestConfig databaseConfig;

    private static ParkingSpotDAO parkingSpotDao;

    private static TicketDAO ticketDAO;

    private static Ticket testTicket;

    private static ParkingSpot testParkingSpot;

    @BeforeAll
    public static void setUp() {
        testTicket = new Ticket();
        testTicket.setInTime(new Date());
        testTicket.setId(1);
        testTicket.setVehicleRegNumber("ABCDEFG");
        testTicket.setPrice(0);

        testParkingSpot = new ParkingSpot(
                1,
                ParkingType.CAR,
                false
        );

        parkingSpotDao = new ParkingSpotDAO();
        ticketDAO = new TicketDAO();
    }

    private void setupParkingDatabaseConnectionFail() {
        try {
            parkingSpotDao.dataBaseConfig = databaseConfig;
            when(databaseConfig.getConnection()).thenThrow(new SQLException());
        }
        catch(Exception e) {
            e.printStackTrace();
            assertTrue(true, "DatabaseConfig connection has failed");
        }
    }

    private void setupTicketDatabaseConnectionFail() {
        try {
            ticketDAO.dataBaseConfig = databaseConfig;
            when(databaseConfig.getConnection()).thenThrow(new SQLException());
        }
        catch(Exception e) {
            e.printStackTrace();
            assertTrue(true, "DatabaseConfig connection has failed");
        }
    }

    /* PARKING DAO TESTS */
    @Test
    public void getParkingNextAvailableSlotCarDatabaseFailedTest() {
        setupParkingDatabaseConnectionFail();

        int slot = parkingSpotDao.getNextAvailableSlot(ParkingType.CAR);
        assertEquals(slot, -1);
    }

    @Test
    public void getParkingNextAvailableSlotBikeDatabaseFailedTest() {
        setupParkingDatabaseConnectionFail();

        int slot = parkingSpotDao.getNextAvailableSlot(ParkingType.BIKE);
        assertEquals(slot, -1);
    }

    @Test
    public void parkingUpdateParkingDatabaseFailedTest() {
        setupParkingDatabaseConnectionFail();

        boolean result = parkingSpotDao.updateParking(testParkingSpot);
        assertEquals(result, false);
    }

    /* TICKET DAO TESTS */
    @Test
    public void ticketSaveDatabaseFailedTest() {
        setupTicketDatabaseConnectionFail();

        boolean result = ticketDAO.saveTicket(testTicket);
        assertEquals(result, false);
    }

    @Test
    public void ticketUpdateDatabaseFailedTest() {
        setupTicketDatabaseConnectionFail();

        boolean result = ticketDAO.updateTicket(testTicket);
        assertEquals(result, false);
    }

    @Test
    public void ticketGetCountDatabaseFailedTest() {
        setupTicketDatabaseConnectionFail();

        int result = ticketDAO.getTicketCount("ABCDEFG");
        assertEquals(result, 0);
    }

    @Test
    public void ticketGetDatabaseFailedTest() {
        setupTicketDatabaseConnectionFail();

        Ticket result = ticketDAO.getTicket("ABCDEFG");
        assertEquals(result, null);
    }
}
