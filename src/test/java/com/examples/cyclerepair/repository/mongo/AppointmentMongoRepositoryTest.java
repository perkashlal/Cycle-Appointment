package com.examples.cyclerepair.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.examples.cyclerepair.model.Appointment;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class AppointmentMongoRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient client;
	private AppointmentMongoRepository appointmentRepository;
	private MongoCollection<Document> appointmentCollection;

	private static final String CYCLE_REPAIR_DB_NAME = "cycle-repair";
	private static final String APPOINTMENT_COLLECTION_NAME = "appointment";

	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		server.shutdown();
	}

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(serverAddress));
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
	public void testFindAllWhenDatabaseIsEmpty() {
		assertThat(appointmentRepository.findAll())
			.isEmpty();
	}

	@Test
	public void testFindAllWhenDatabaseIsNotEmpty() {
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
	public void testFindByIdNotFound() {
		assertThat(appointmentRepository.findById("1"))
			.isNull();
	}

	@Test
	public void testFindByIdFound() {
		addTestAppointmentToDatabase("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		addTestAppointmentToDatabase("2", "Luigi Bianchi", "City Bike",
				"Flat tyre", "2026-06-11");
		assertThat(appointmentRepository.findById("2"))
			.isEqualTo(new Appointment("2", "Luigi Bianchi", "City Bike",
					"Flat tyre", "2026-06-11"));
	}

	@Test
	public void testSave() {
		Appointment appointment = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		appointmentRepository.save(appointment);
		assertThat(readAllAppointmentsFromDatabase())
			.containsExactly(appointment);
	}

	@Test
	public void testDelete() {
		addTestAppointmentToDatabase("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		appointmentRepository.delete("1");
		assertThat(readAllAppointmentsFromDatabase())
			.isEmpty();
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

	private List<Appointment> readAllAppointmentsFromDatabase() {
		return StreamSupport.
			stream(appointmentCollection.find().spliterator(), false)
				.map(d -> new Appointment(""+d.get("id"), ""+d.get("customerName"),
						""+d.get("cycleModel"), ""+d.get("repairIssue"),
						""+d.get("appointmentDate")))
				.collect(Collectors.toList());
	}
}
