package com.examples.cyclerepair.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class AppointmentMongoRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient client;
	private AppointmentMongoRepository appointmentRepository;

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
}
