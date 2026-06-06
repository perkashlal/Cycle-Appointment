package com.examples.cyclerepair.view;

import java.util.List;

import com.examples.cyclerepair.model.Appointment;

public interface AppointmentView {

	void showAllAppointments(List<Appointment> appointments);

	void showError(String message, Appointment appointment);

	void appointmentAdded(Appointment appointment);

	void appointmentRemoved(Appointment appointment);

}
