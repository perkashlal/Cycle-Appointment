package com.examples.cyclerepair.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.examples.cyclerepair.model.Appointment;
import com.examples.cyclerepair.repository.AppointmentRepository;
import com.examples.cyclerepair.view.AppointmentView;

public class CycleRepairControllerTest {

	@Mock
	private AppointmentRepository appointmentRepository;

	@Mock
	private AppointmentView appointmentView;

	@InjectMocks
	private CycleRepairController cycleRepairController;

	private AutoCloseable closeable;

	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllAppointments() {
		List<Appointment> appointments = asList(new Appointment("1", "Mario Rossi",
				"Road Bike", "Brake adjustment", "2026-06-10"));
		when(appointmentRepository.findAll())
			.thenReturn(appointments);
		cycleRepairController.allAppointments();
		verify(appointmentView)
			.showAllAppointments(appointments);
	}
}
