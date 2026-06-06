package com.examples.cyclerepair.repository;

import java.util.List;

import com.examples.cyclerepair.model.Appointment;

public interface AppointmentRepository {
	public List<Appointment> findAll();

	public Appointment findById(String id);

	public void save(Appointment appointment);

	public void delete(String id);
}
