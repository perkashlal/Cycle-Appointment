package com.examples.cyclerepair.view.swing;

import java.util.List;

import javax.swing.JFrame;

import com.examples.cyclerepair.controller.CycleRepairController;
import com.examples.cyclerepair.model.Appointment;
import com.examples.cyclerepair.view.AppointmentView;

public class AppointmentSwingView extends JFrame implements AppointmentView {

	private static final long serialVersionUID = 1L;

	private transient CycleRepairController cycleRepairController;

	public void setCycleRepairController(CycleRepairController cycleRepairController) {
		this.cycleRepairController = cycleRepairController;
	}

	@Override
	public void showAllAppointments(List<Appointment> appointments) {
	}

	@Override
	public void showError(String message, Appointment appointment) {
	}

	@Override
	public void appointmentAdded(Appointment appointment) {
	}

	@Override
	public void appointmentRemoved(Appointment appointment) {
	}

	@Override
	public void showErrorAppointmentNotFound(String message, Appointment appointment) {
	}
}
