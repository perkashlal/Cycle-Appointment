package com.examples.cyclerepair.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.examples.cyclerepair.controller.CycleRepairController;
import com.examples.cyclerepair.model.Appointment;
import com.examples.cyclerepair.repository.mongo.AppointmentMongoRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

@RunWith(GUITestRunner.class)
public class AppointmentSwingViewIT extends AssertJSwingJUnitTestCase {
	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient mongoClient;

	private FrameFixture window;
	private AppointmentSwingView appointmentSwingView;
	private CycleRepairController cycleRepairController;
	private AppointmentMongoRepository appointmentRepository;

	private static final String CYCLE_REPAIR_DB_NAME = "cycle-repair";
	private static final String APPOINTMENT_COLLECTION_NAME = "appointment";

	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		// bind on a random local port
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		server.shutdown();
	}

	@Override
	protected void onSetUp() {
		mongoClient = new MongoClient(new ServerAddress(serverAddress));
		appointmentRepository =
			new AppointmentMongoRepository(mongoClient, CYCLE_REPAIR_DB_NAME, APPOINTMENT_COLLECTION_NAME);
		// explicit empty the database through the repository
		for (Appointment appointment : appointmentRepository.findAll()) {
			appointmentRepository.delete(appointment.getId());
		}
		GuiActionRunner.execute(() -> {
			appointmentSwingView = new AppointmentSwingView();
			cycleRepairController = new CycleRepairController(appointmentSwingView, appointmentRepository);
			appointmentSwingView.setCycleRepairController(cycleRepairController);
			return appointmentSwingView;
		});
		window = new FrameFixture(robot(), appointmentSwingView);
		window.show(); // shows the frame to test
	}

	@Override
	protected void onTearDown() {
		mongoClient.close();
	}

	@Test @GUITest
	public void testAllAppointments() {
		// use the repository to add appointments to the database
		Appointment appointment1 = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		Appointment appointment2 = new Appointment("2", "Luigi Bianchi", "City Bike",
				"Flat tyre", "2026-06-11");
		appointmentRepository.save(appointment1);
		appointmentRepository.save(appointment2);
		// use the controller's allAppointments
		GuiActionRunner.execute(
			() -> cycleRepairController.allAppointments());
		// and verify that the view's list is populated
		assertThat(window.list().contents())
			.containsExactly(
					"1 - Mario Rossi - Road Bike - Brake adjustment - 2026-06-10",
					"2 - Luigi Bianchi - City Bike - Flat tyre - 2026-06-11");
	}

	@Test @GUITest
	public void testAddButtonSuccess() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("customerNameTextBox").enterText("Mario Rossi");
		window.textBox("cycleModelTextBox").enterText("Road Bike");
		window.textBox("repairIssueTextBox").enterText("Brake adjustment");
		window.textBox("appointmentDateTextBox").enterText("2026-06-10");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents())
			.containsExactly("1 - Mario Rossi - Road Bike - Brake adjustment - 2026-06-10");
	}

	@Test @GUITest
	public void testAddButtonError() {
		appointmentRepository.save(new Appointment("1", "Existing Customer", "City Bike",
				"Flat tyre", "2026-06-11"));
		window.textBox("idTextBox").enterText("1");
		window.textBox("customerNameTextBox").enterText("Mario Rossi");
		window.textBox("cycleModelTextBox").enterText("Road Bike");
		window.textBox("repairIssueTextBox").enterText("Brake adjustment");
		window.textBox("appointmentDateTextBox").enterText("2026-06-10");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents())
			.isEmpty();
		window.label("errorMessageLabel")
			.requireText("Already existing appointment with id 1: "
					+ "1 - Existing Customer - City Bike - Flat tyre - 2026-06-11");
	}

	@Test @GUITest
	public void testDeleteButtonSuccess() {
		// use the controller to populate the view's list...
		GuiActionRunner.execute(
			() -> cycleRepairController.newAppointment(new Appointment("1", "Mario Rossi",
					"Road Bike", "Brake adjustment", "2026-06-10")));
		// ...with an appointment to select
		window.list().selectItem(0);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		assertThat(window.list().contents())
			.isEmpty();
	}

	@Test @GUITest
	public void testDeleteButtonError() {
		// manually add an appointment to the list, which will not be in the db
		Appointment appointment = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		GuiActionRunner.execute(
			() -> appointmentSwingView.getListAppointmentsModel().addElement(appointment));
		window.list().selectItem(0);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		assertThat(window.list().contents())
			.isEmpty();
		window.label("errorMessageLabel")
			.requireText("No existing appointment with id 1: "
					+ "1 - Mario Rossi - Road Bike - Brake adjustment - 2026-06-10");
	}
}
