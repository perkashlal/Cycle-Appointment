package com.examples.cyclerepair.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.examples.cyclerepair.model.Appointment;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class AppointmentMongoRepositoryTestcontainersIT {

	@ClassRule
	public static final MongoDBContainer mongo =
		new MongoDBContainer("mongo:5");

	private MongoClient client;
	private AppointmentMongoRepository appointmentRepository;
	private MongoCollection<Document> appointmentCollection;

	private static final String CYCLE_REPAIR_DB_NAME = "cycle-repair";
	private static final String APPOINTMENT_COLLECTION_NAME = "appointment";

	@Before
	public void setup() {
		client = new MongoClient(
			new ServerAddress(
				mongo.getHost(),
				mongo.getFirstMappedPort()));
		appointmentRepository =
			new AppointmentMongoRepository(client, CYCLE_REPAIR_DB_NAME, APPOINTMENT_COLLECTION_NAME);
		MongoDatabase database = client.getDatabase(CYCLE_REPAIR_DB_NAME);
		database.drop();
		appointmentCollection = database.getCollection(APPOINTMENT_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAll() {
		addTestAppointmentToDatabase("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		addTestAppointmentToDatabase("2", "Luigi Bianchi", "City Bike",
				"Flat tyre", "2026-06-11");
		assertThat(appointmentRepository.findAll())
			.containsExactly(
				new Appointment("1", "Mario Rossi", "Road Bike",
						"Brake adjustment", "2026-06-10"),
				new Appointment("2", "Luigi Bianchi", "City Bike",
						"Flat tyre", "2026-06-11"));
	}

	@Test
	public void testFindById() {
		addTestAppointmentToDatabase("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		addTestAppointmentToDatabase("2", "Luigi Bianchi", "City Bike",
				"Flat tyre", "2026-06-11");
		assertThat(appointmentRepository.findById("2"))
			.isEqualTo(new Appointment("2", "Luigi Bianchi", "City Bike",
					"Flat tyre", "2026-06-11"));
	}

	private void addTestAppointmentToDatabase(String id, String customerName, String cycleModel,
			String repairIssue, String appointmentDate) {
		appointmentCollection.insertOne(
				new Document()
					.append("id", id)
					.append("customerName", customerName)
					.append("cycleModel", cycleModel)
					.append("repairIssue", repairIssue)
					.append("appointmentDate", appointmentDate));
	}
}
