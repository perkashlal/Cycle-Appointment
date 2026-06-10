package com.examples.cyclerepair.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class AppointmentTest {

	@Test
	public void testToString() {
		Appointment appointment = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");

		assertThat(appointment)
			.hasToString("Appointment [id=1, customerName=Mario Rossi, cycleModel=Road Bike, "
					+ "repairIssue=Brake adjustment, appointmentDate=2026-06-10]");
	}

	@Test
	public void testEquals() {
		Appointment appointment = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		Appointment sameAppointment = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");

		assertThat(appointment)
			.isEqualTo(sameAppointment);
	}

	@Test
	public void testHashCode() {
		Appointment appointment = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		Appointment sameAppointment = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");

		assertThat(appointment)
			.hasSameHashCodeAs(sameAppointment);
	}
}
