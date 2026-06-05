package com.examples.cyclerepair.repository;

import java.util.List;

import com.examples.cyclerepair.model.Appointment;

public interface AppointmentRepository {
	public List<Appointment> findAll();
}
