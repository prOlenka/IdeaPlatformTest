package idea.platform.test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Root{
    public ArrayList<Ticket> tickets;

    public Root(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Map<String, Duration> getMinFlightTimePerCarrier() {
        return tickets.stream()
                .filter(ticket -> ticket.getOrigin().equals("VVO") && ticket.getDestination().equals("TLV"))
                .collect(Collectors.groupingBy(
                        Ticket::getCarrier,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .map(ticket -> calculateDuration(ticket.getDepartureTime(), ticket.getArrivalTime()))
                                        .min(Duration::compareTo)
                                        .orElse(Duration.ZERO)
                        )
                ));
    }

    public static Duration calculateDuration(LocalDateTime departure, LocalDateTime arrival) {
        if (departure == null || arrival == null) {
            throw new IllegalArgumentException("Время прибытия и отлёта не могут быть пустыми");
        }
        return Duration.between(departure, arrival);
    }

    public double getPriceDifference() {
        List<Integer> prices = tickets.stream()
                .filter(ticket -> ticket.getOrigin().equals("VVO") && ticket.getDestination().equals("TLV"))
                .map(Ticket::getPrice)
                .sorted()
                .collect(Collectors.toList());

        if (prices.isEmpty()) {
            return 0.0;
        }

        double median = calculateMedian(prices);
        double min = prices
                .stream()
                .mapToInt(Integer::intValue)
                .average()
                        .orElse(0);

        return min - median;
    }

    private double calculateMedian(List<Integer> prices) {
        int size = prices.size();
        if (size % 2 == 0) {
            return (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        } else {
            return prices.get(size / 2);
        }
    }
}
