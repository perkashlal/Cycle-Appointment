package com.examples.cyclerepair.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;

@RunWith(GUITestRunner.class)
public class CycleRepairSwingAppE2E // NOSONAR we want the name this way
	extends AssertJSwingJUnitTestCase {

	@ClassRule
	public static final MongoDBContainer mongo =
		new MongoDBContainer("mongo:5");

	private static final String DB_NAME = "test-db";
	private static final String COLLECTION_NAME = "test-collection";

	private static final String APPOINTMENT_FIXTURE_1_ID = "1";
	private static final String APPOINTMENT_FIXTURE_1_CUSTOMER_NAME = "Mario Rossi";
	private static final String APPOINTMENT_FIXTURE_1_CYCLE_MODEL = "Road Bike";
	private static final String APPOINTMENT_FIXTURE_1_REPAIR_ISSUE = "Brake adjustment";
	private static final String APPOINTMENT_FIXTURE_1_APPOINTMENT_DATE = "2026-06-10";
	private static final String APPOINTMENT_FIXTURE_2_ID = "2";
	private static final String APPOINTMENT_FIXTURE_2_CUSTOMER_NAME = "Luigi Bianchi";
	private static final String APPOINTMENT_FIXTURE_2_CYCLE_MODEL = "City Bike";
	private static final String APPOINTMENT_FIXTURE_2_REPAIR_ISSUE = "Flat tyre";
	private static final String APPOINTMENT_FIXTURE_2_APPOINTMENT_DATE = "2026-06-11";

	private MongoClient mongoClient;

	private FrameFixture window;

	@Override
	protected void onSetUp() {
		String containerIpAddress = mongo.getHost();
		Integer mappedPort = mongo.getFirstMappedPort();
		mongoClient = new MongoClient(containerIpAddress, mappedPort);
		// always start with an empty database
		mongoClient.getDatabase(DB_NAME).drop();
		// add some appointments to the database
		addTestAppointmentToDatabase(APPOINTMENT_FIXTURE_1_ID,
				APPOINTMENT_FIXTURE_1_CUSTOMER_NAME, APPOINTMENT_FIXTURE_1_CYCLE_MODEL,
				APPOINTMENT_FIXTURE_1_REPAIR_ISSUE, APPOINTMENT_FIXTURE_1_APPOINTMENT_DATE);
		addTestAppointmentToDatabase(APPOINTMENT_FIXTURE_2_ID,
				APPOINTMENT_FIXTURE_2_CUSTOMER_NAME, APPOINTMENT_FIXTURE_2_CYCLE_MODEL,
				APPOINTMENT_FIXTURE_2_REPAIR_ISSUE, APPOINTMENT_FIXTURE_2_APPOINTMENT_DATE);
		// start the Swing application
		application("com.examples.cyclerepair.app.swing.CycleRepairSwingApp")
			.withArgs(
				"--mongo-host=" + containerIpAddress,
				"--mongo-port=" + mappedPort.toString(),
				"--db-name=" + DB_NAME,
				"--db-collection=" + COLLECTION_NAME
			)
			.start();
		// get a reference of its JFrame
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Appointment View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
		window.focus();
	}

	@Override
	protected void onTearDown() {
		mongoClient.close();
	}

	@Test @GUITest
	public void testOnStartAllDatabaseElementsAreShown() {
		assertThat(window.list().contents())
			.anySatisfy(e -> assertThat(e).contains(APPOINTMENT_FIXTURE_1_ID,
					APPOINTMENT_FIXTURE_1_CUSTOMER_NAME))
			.anySatisfy(e -> assertThat(e).contains(APPOINTMENT_FIXTURE_2_ID,
					APPOINTMENT_FIXTURE_2_CUSTOMER_NAME));
	}

	@Test @GUITest
	public void testAddButtonSuccess() {
		window.textBox("idTextBox").enterText("10");
		window.textBox("customerNameTextBox").enterText("New Customer");
		window.textBox("cycleModelTextBox").enterText("Gravel Bike");
		window.textBox("repairIssueTextBox").enterText("Gear tuning");
		window.textBox("appointmentDateTextBox").enterText("2026-06-12");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents())
			.anySatisfy(e -> assertThat(e).contains("10", "New Customer",
					"Gravel Bike", "Gear tuning", "2026-06-12"));
	}

	@Test @GUITest
	public void testAddButtonError() {
		window.textBox("idTextBox").enterText(APPOINTMENT_FIXTURE_1_ID);
		window.textBox("customerNameTextBox").enterText("New Customer");
		window.textBox("cycleModelTextBox").enterText("Gravel Bike");
		window.textBox("repairIssueTextBox").enterText("Gear tuning");
		window.textBox("appointmentDateTextBox").enterText("2026-06-12");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.label("errorMessageLabel").text())
			.contains(APPOINTMENT_FIXTURE_1_ID, APPOINTMENT_FIXTURE_1_CUSTOMER_NAME);
	}

	@Test @GUITest
	public void testDeleteButtonSuccess() {
		window.list("appointmentList")
			.selectItem(Pattern.compile(".*" + APPOINTMENT_FIXTURE_1_CUSTOMER_NAME + ".*"));
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		assertThat(window.list().contents())
			.noneMatch(e -> e.contains(APPOINTMENT_FIXTURE_1_CUSTOMER_NAME));
	}

	@Test @GUITest
	public void testDeleteButtonError() {
		// select the appointment in the list...
		window.list("appointmentList")
			.selectItem(Pattern.compile(".*" + APPOINTMENT_FIXTURE_1_CUSTOMER_NAME + ".*"));
		// ... in the meantime, manually remove the appointment from the database
		removeTestAppointmentFromDatabase(APPOINTMENT_FIXTURE_1_ID);
		// now press the delete button
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		// and verify an error is shown
		assertThat(window.label("errorMessageLabel").text())
			.contains(APPOINTMENT_FIXTURE_1_ID, APPOINTMENT_FIXTURE_1_CUSTOMER_NAME);
	}

	private void addTestAppointmentToDatabase(String id, String customerName, String cycleModel,
			String repairIssue, String appointmentDate) {
		mongoClient
			.getDatabase(DB_NAME)
			.getCollection(COLLECTION_NAME)
			.insertOne(
				new Document()
					.append("id", id)
					.append("customerName", customerName)
					.append("cycleModel", cycleModel)
					.append("repairIssue", repairIssue)
					.append("appointmentDate", appointmentDate));
	}

	private void removeTestAppointmentFromDatabase(String id) {
		mongoClient
			.getDatabase(DB_NAME)
			.getCollection(COLLECTION_NAME)
			.deleteOne(Filters.eq("id", id));
	}
}
