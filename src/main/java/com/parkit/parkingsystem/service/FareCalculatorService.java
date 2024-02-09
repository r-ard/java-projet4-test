package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.DateUtil;

public class FareCalculatorService {
    private TicketDAO ticketDAO;

    public FareCalculatorService() {
        this.ticketDAO = null;
    }

    public void setTicketDAO(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double duration = (double)DateUtil.getDatesDiffInMinutes(ticket.getOutTime(), ticket.getInTime()) / 60.0D;

        // Apply 30 minutes reduction
        if(duration <= 0.5D) duration = 0.0D;
        else duration -= 0.5D;

        double priceFactor = 1.0D;

        // If the vehicule has already a registered ticket in the DB, then apply a 5% reduction to the price
        if(this.ticketDAO != null
                && this.ticketDAO.getTicketCount(ticket.getVehicleRegNumber()) > 1) {
            priceFactor = 0.95D;
        }

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * priceFactor);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * priceFactor);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}