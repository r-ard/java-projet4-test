package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TicketDataBaseTest {
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    private static TicketDAO ticketDAO;

    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    public static void setUp() throws Exception{
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @AfterEach
    public void postTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    private Ticket generateTemplateTicket(ParkingType type) {
        Ticket ticket = new Ticket();

        ticket.setId(0);
        ticket.setParkingSpot(new ParkingSpot(1, type, true));
        ticket.setVehicleRegNumber("ABCDEFGH");
        ticket.setInTime(new Date());
        ticket.setOutTime(null);
        ticket.setPrice(0);

        return ticket;
    }

    @Test
    public void ticketCarSaveTest() {
        Ticket ticket = this.generateTemplateTicket(ParkingType.CAR);

        boolean flag = ticketDAO.saveTicket(ticket);
        assertEquals(flag, true);
    }

    @Test
    public void ticketCarGetTicketTest() {
        Ticket ticket = this.generateTemplateTicket(ParkingType.CAR);

        boolean flag = ticketDAO.saveTicket(ticket);
        assertEquals(flag, true);

        Ticket dbTicket = ticketDAO.getTicket(ticket.getVehicleRegNumber());
        assertNotEquals(dbTicket, null);
    }

    @Test
    public void ticketCarUpdateTest() {
        Ticket ticket = this.generateTemplateTicket(ParkingType.CAR);

        boolean saveResult = ticketDAO.saveTicket(ticket);
        assertEquals(saveResult, true);

        Ticket dbTicket = ticketDAO.getTicket(ticket.getVehicleRegNumber());
        assertNotEquals(dbTicket, null);

        dbTicket.setOutTime(new Date());

        boolean updateResult = ticketDAO.updateTicket(dbTicket);
        assertEquals(updateResult, true);
    }

    @Test
    public void ticketCarGetTicketCountTest() {
        Ticket ticket = this.generateTemplateTicket(ParkingType.CAR);

        boolean flag = ticketDAO.saveTicket(ticket);
        assertEquals(flag, true);

        int count = ticketDAO.getTicketCount(ticket.getVehicleRegNumber());
        assertEquals(count, 1);
    }
}
