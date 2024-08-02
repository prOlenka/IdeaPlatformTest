package idea.platform.test;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class Ticket{
    public String origin;
    public String destination;
    public LocalDateTime departureTime;
    public LocalDateTime arrivalTime;
    public String carrier;
    public int price;

    public Ticket(String origin, String destination, LocalDateTime departureTime, LocalDateTime arrivalTime, String carrier, int price) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.carrier = carrier;
        this.price = price;
    }

}