package com.examples.cyclerepair.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.examples.cyclerepair.controller.CycleRepairController;
import com.examples.cyclerepair.model.Appointment;

@RunWith(GUITestRunner.class)
public class AppointmentSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	private AppointmentSwingView appointmentSwingView;

	@Mock
	private CycleRepairController cycleRepairController;

	private AutoCloseable closeable;

	@Override
	protected void onSetUp() {
		closeable = MockitoAnnotations.openMocks(this);
		GuiActionRunner.execute(() -> {
			appointmentSwingView = new AppointmentSwingView();
			appointmentSwingView.setCycleRepairController(cycleRepairController);
			return appointmentSwingView;
		});
		window = new FrameFixture(robot(), appointmentSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test @GUITest
	public void testControlsInitialStates() {
		window.label(JLabelMatcher.withText("id"));
		window.textBox("idTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("customer name"));
		window.textBox("customerNameTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("cycle model"));
		window.textBox("cycleModelTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("repair issue"));
		window.textBox("repairIssueTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("appointment date"));
		window.textBox("appointmentDateTextBox").requireEnabled();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.list("appointmentList");
		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testWhenAllFieldsAreNonEmptyThenAddButtonShouldBeEnabled() {
		enterAppointmentData("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
	}

	@Test
	public void testWhenAnyFieldIsBlankThenAddButtonShouldBeDisabled() {
		String[][] appointmentDataWithOneBlankField = {
				{ " ", "Mario Rossi", "Road Bike", "Brake adjustment", "2026-06-10" },
				{ "1", " ", "Road Bike", "Brake adjustment", "2026-06-10" },
				{ "1", "Mario Rossi", " ", "Brake adjustment", "2026-06-10" },
				{ "1", "Mario Rossi", "Road Bike", " ", "2026-06-10" },
				{ "1", "Mario Rossi", "Road Bike", "Brake adjustment", " " }
		};

		for (String[] appointmentData : appointmentDataWithOneBlankField) {
			enterAppointmentData(appointmentData[0], appointmentData[1],
					appointmentData[2], appointmentData[3], appointmentData[4]);
			window.button(JButtonMatcher.withText("Add")).requireDisabled();
			clearAppointmentData();
		}
	}

	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenAnAppointmentIsSelected() {
		Appointment appointment = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		GuiActionRunner.execute(
			() -> appointmentSwingView.getListAppointmentsModel().addElement(appointment)
		);
		window.list("appointmentList").selectItem(0);
		JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete Selected"));
		deleteButton.requireEnabled();
		window.list("appointmentList").clearSelection();
		deleteButton.requireDisabled();
	}

	@Test
	public void testsShowAllAppointmentsShouldAddAppointmentDescriptionsToTheList() {
		Appointment appointment1 = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		Appointment appointment2 = new Appointment("2", "Luigi Bianchi", "City Bike",
				"Flat tyre", "2026-06-11");
		GuiActionRunner.execute(
			() -> appointmentSwingView.showAllAppointments(
					Arrays.asList(appointment1, appointment2))
		);
		String[] listContents = window.list().contents();
		assertThat(listContents)
			.containsExactly(
					"1 - Mario Rossi - Road Bike - Brake adjustment - 2026-06-10",
					"2 - Luigi Bianchi - City Bike - Flat tyre - 2026-06-11");
	}

	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		Appointment appointment = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		GuiActionRunner.execute(
			() -> appointmentSwingView.showError("error message", appointment)
		);
		window.label("errorMessageLabel")
			.requireText("error message: 1 - Mario Rossi - Road Bike - Brake adjustment - 2026-06-10");
	}

	@Test
	public void testShowErrorAppointmentNotFound() {
		Appointment appointment1 = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		Appointment appointment2 = new Appointment("2", "Luigi Bianchi", "City Bike",
				"Flat tyre", "2026-06-11");
		GuiActionRunner.execute(
			() -> {
				DefaultListModel<Appointment> listAppointmentsModel =
						appointmentSwingView.getListAppointmentsModel();
				listAppointmentsModel.addElement(appointment1);
				listAppointmentsModel.addElement(appointment2);
			}
		);
		GuiActionRunner.execute(
			() -> appointmentSwingView.showErrorAppointmentNotFound("error message", appointment1)
		);
		window.label("errorMessageLabel")
			.requireText("error message: 1 - Mario Rossi - Road Bike - Brake adjustment - 2026-06-10");
		assertThat(window.list().contents())
			.containsExactly("2 - Luigi Bianchi - City Bike - Flat tyre - 2026-06-11");
	}

	@Test
	public void testAppointmentAddedShouldAddTheAppointmentToTheListAndResetTheErrorLabel() {
		Appointment appointment = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		GuiActionRunner.execute(
				() -> appointmentSwingView.appointmentAdded(appointment)
				);
		String[] listContents = window.list().contents();
		assertThat(listContents)
			.containsExactly("1 - Mario Rossi - Road Bike - Brake adjustment - 2026-06-10");
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testAppointmentRemovedShouldRemoveTheAppointmentFromTheListAndResetTheErrorLabel() {
		Appointment appointment1 = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		Appointment appointment2 = new Appointment("2", "Luigi Bianchi", "City Bike",
				"Flat tyre", "2026-06-11");
		GuiActionRunner.execute(
			() -> {
				DefaultListModel<Appointment> listAppointmentsModel =
						appointmentSwingView.getListAppointmentsModel();
				listAppointmentsModel.addElement(appointment1);
				listAppointmentsModel.addElement(appointment2);
			}
		);
		GuiActionRunner.execute(
			() -> appointmentSwingView.appointmentRemoved(appointment1)
		);
		String[] listContents = window.list().contents();
		assertThat(listContents)
			.containsExactly("2 - Luigi Bianchi - City Bike - Flat tyre - 2026-06-11");
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testAddButtonShouldDelegateToCycleRepairControllerNewAppointment() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("customerNameTextBox").enterText("Mario Rossi");
		window.textBox("cycleModelTextBox").enterText("Road Bike");
		window.textBox("repairIssueTextBox").enterText("Brake adjustment");
		window.textBox("appointmentDateTextBox").enterText("2026-06-10");
		window.button(JButtonMatcher.withText("Add")).click();
		verify(cycleRepairController).newAppointment(new Appointment("1", "Mario Rossi",
				"Road Bike", "Brake adjustment", "2026-06-10"));
	}

	@Test
	public void testDeleteButtonShouldDelegateToCycleRepairControllerDeleteAppointment() {
		Appointment appointment1 = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		Appointment appointment2 = new Appointment("2", "Luigi Bianchi", "City Bike",
				"Flat tyre", "2026-06-11");
		GuiActionRunner.execute(
			() -> {
				DefaultListModel<Appointment> listAppointmentsModel =
						appointmentSwingView.getListAppointmentsModel();
				listAppointmentsModel.addElement(appointment1);
				listAppointmentsModel.addElement(appointment2);
			}
		);
		window.list("appointmentList").selectItem(1);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		verify(cycleRepairController).deleteAppointment(appointment2);
	}

	private void enterAppointmentData(String id, String customerName, String cycleModel,
			String repairIssue, String appointmentDate) {
		window.textBox("idTextBox").enterText(id);
		window.textBox("customerNameTextBox").enterText(customerName);
		window.textBox("cycleModelTextBox").enterText(cycleModel);
		window.textBox("repairIssueTextBox").enterText(repairIssue);
		window.textBox("appointmentDateTextBox").enterText(appointmentDate);
	}

	private void clearAppointmentData() {
		window.textBox("idTextBox").setText("");
		window.textBox("customerNameTextBox").setText("");
		window.textBox("cycleModelTextBox").setText("");
		window.textBox("repairIssueTextBox").setText("");
		window.textBox("appointmentDateTextBox").setText("");
	}
}
