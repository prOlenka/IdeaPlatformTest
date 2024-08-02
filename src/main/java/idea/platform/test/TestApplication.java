package idea.platform.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.Main;

import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

public class TestApplication {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy H:mm");

	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();

		try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("tickets.json")) {
			if (inputStream == null) {
				System.out.println("Файл tickets.json не найден");
			}

			JsonNode root = mapper.readTree(inputStream);
			JsonNode ticketNodes = root.get("tickets");

			ArrayList<Ticket> tickets = new ArrayList<>();
			for (JsonNode node : ticketNodes) {
				String origin = node.get("origin").asText();
				String destination = node.get("destination").asText();
				LocalDateTime departureDate = LocalDateTime.parse(node.get("departure_date").asText() + " " + node.get("departure_time").asText(), DATE_TIME_FORMATTER);
				LocalDateTime arrivalDate = LocalDateTime.parse(node.get("arrival_date").asText() + " " + node.get("arrival_time").asText(), DATE_TIME_FORMATTER);
				String carrier = node.get("carrier").asText();
				int price = node.get("price").asInt();

				tickets.add(new Ticket(origin, destination, departureDate, arrivalDate, carrier, price));
			}

			Root flightService = new Root(tickets);

			Map<String, Duration> minFlightTimes = flightService.getMinFlightTimePerCarrier();
			System.out.println("Минимальное время полёта для каждого перевозчика:");
			minFlightTimes.forEach((carrier, duration) ->
					System.out.printf("Перевозчик: %s, Минимальное время полёта: %d hours %d minutes%n",
							carrier, duration.toHours(), duration.toMinutes() % 60)
			);

			System.out.printf("Разница между ср.ценой и медианной: " + flightService.getPriceDifference());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
