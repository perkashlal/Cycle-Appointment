package com.examples.cyclerepair.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.examples.cyclerepair.controller.CycleRepairController;

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
		JTextComponentFixture idTextBox = window.textBox("idTextBox");
		JTextComponentFixture customerNameTextBox = window.textBox("customerNameTextBox");
		JTextComponentFixture cycleModelTextBox = window.textBox("cycleModelTextBox");
		JTextComponentFixture repairIssueTextBox = window.textBox("repairIssueTextBox");
		JTextComponentFixture appointmentDateTextBox = window.textBox("appointmentDateTextBox");

		idTextBox.enterText("1");
		customerNameTextBox.enterText("Mario Rossi");
		cycleModelTextBox.enterText("Road Bike");
		repairIssueTextBox.enterText("Brake adjustment");
		appointmentDateTextBox.enterText("2026-06-10");

		window.button(JButtonMatcher.withText("Add")).requireEnabled();
	}
}
