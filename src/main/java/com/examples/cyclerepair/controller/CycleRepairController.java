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
		if (appointmentRepository.findById(appointment.getId()) == null) {
			appointmentRepository.save(appointment);
			appointmentView.appointmentAdded(appointment);
		}
	}
}
