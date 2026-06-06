package com.examples.cyclerepair.controller;

import com.examples.cyclerepair.repository.AppointmentRepository;
import com.examples.cyclerepair.view.AppointmentView;
import com.examples.cyclerepair.model.Appointment;

public class CycleRepairController {
	private AppointmentView appointmentView;
	private AppointmentRepository appointmentRepository;

	public CycleRepairController(AppointmentView appointmentView,
			AppointmentRepository appointmentRepository) {
		this.appointmentView = appointmentView;
		this.appointmentRepository = appointmentRepository;
	}

	public void allAppointments() {
		appointmentView.showAllAppointments(appointmentRepository.findAll());
	}

	public void newAppointment(Appointment appointment) {
		Appointment existingAppointment = appointmentRepository.findById(appointment.getId());
		if (existingAppointment != null) {
			appointmentView.showError("Already existing appointment with id " + appointment.getId(),
					existingAppointment);
			return;
		}

		appointmentRepository.save(appointment);
		appointmentView.appointmentAdded(appointment);
	}

	public void deleteAppointment(Appointment appointment) {

	}
}
