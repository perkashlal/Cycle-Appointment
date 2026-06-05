package com.examples.cyclerepair.controller;

import com.examples.cyclerepair.repository.AppointmentRepository;
import com.examples.cyclerepair.view.AppointmentView;

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
}
