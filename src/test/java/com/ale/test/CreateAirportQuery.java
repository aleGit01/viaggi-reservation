package com.ale.test;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class CreateAirportQuery {

	@Test
	public void testInsert() throws IOException {
		try (Stream<String> stream = Files.lines(Paths.get(URI.create("file:/D:/dev/airports/airports.dat")))) {

			List<String> lines = stream.collect(Collectors.toList());

			int counter = 0;
			for (String line : lines) {
				counter = counter + createInsert(line);
			}

			System.out.println(counter);
		}
	}

	public int createInsert(String line) {

		List<String> fiels = Stream.of(line.split(",")).map(elem -> new String(elem)).collect(Collectors.toList());

		StringBuilder builder = new StringBuilder();
		builder.append("insert into viaggi_reserv.airport ");
		builder.append(
				"(name,city, country, iata_code, icao_code, latitude, longitude, altitude, timezone_offset, dst, timezone_tz)");

		builder.append(" value (");
		generateStringValue(builder, fiels.get(1), true);
		generateStringValue(builder, fiels.get(2), false);
		generateStringValue(builder, fiels.get(3), false); // country
		generateStringValue(builder, fiels.get(4), false); // iata_code
		generateStringValue(builder, fiels.get(5), false);

		builder.append(", ").append(fiels.get(6));
		builder.append(", ").append(fiels.get(7));
		builder.append(", ").append(fiels.get(8));
		generateStringValue(builder, fiels.get(9), false);
		generateStringValue(builder, fiels.get(10), false);

		generateStringValue(builder, fiels.get(11), false);

		builder.append(");");

		if (!"\\N".equals(fiels.get(4))) { // only with iata_code
			System.out.println(builder.toString());
			return 1;
		}

		return 0;

	}

	private void generateStringValue(StringBuilder builder, String value, boolean isFirst) {

		if (!isFirst) {
			builder.append(", ");
		}
		if ("\\N".equals(value)) {
			builder.append("null");
		} else {
			String valueWithoutSpecialChars = value.replaceAll("\"", "").replaceAll("\'", "");
			builder.append("\'").append(valueWithoutSpecialChars).append("\'");
		}
	}

}
