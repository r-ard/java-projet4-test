package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.DateUtil;

public class FareCalculatorService {
    private final double THIRTY_MINUTES_IN_HOUR = 0.5D;
    private final double REDUCTION_AMOUNT = 0.05D; // 5%

    private TicketDAO ticketDAO;

    public FareCalculatorService() {
        this.ticketDAO = null;
    }

    public void setTicketDAO(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    public void calculateFare(Ticket ticket, boolean applyReduction){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double duration = (double)DateUtil.getDatesDiffInMinutes(ticket.getOutTime(), ticket.getInTime()) / 60.0D;

        // Apply 30 free minutes
        if(duration <= THIRTY_MINUTES_IN_HOUR) duration = 0.0D;

        double priceFactor = 1.0D; // Default 100%

        // If the applyReduction flag is set to TRUE, then apply a 5% reduction to the price
        if(applyReduction) {
            priceFactor -= REDUCTION_AMOUNT; // Apply the reduction
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