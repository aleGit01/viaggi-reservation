package com.ale.viaggi.reservation.avro;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.ale.viaggi.reservation.avro.event.ReservationEvent;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;

public class AvroSchemaGeneratorReservation {

	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper(new AvroFactory());
		AvroSchemaGenerator gen = new AvroSchemaGenerator();
		try {
			mapper.acceptJsonFormatVisitor(ReservationEvent.class, gen);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AvroSchema schemaWrapper = gen.getGeneratedSchema();

		org.apache.avro.Schema avroSchema = schemaWrapper.getAvroSchema();
		String asJson = avroSchema.toString(true);

		System.out.println(asJson);

		// TODO:
		Path path = Paths.get("src/main/java/com/ale/viaggi/reservation/avro/ReservationEvent.avsc");

		// Use try-with-resource to get auto-closeable writer instance
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(asJson);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
