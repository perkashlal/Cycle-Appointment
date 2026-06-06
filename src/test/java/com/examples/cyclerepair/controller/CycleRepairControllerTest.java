package com.examples.cyclerepair.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
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

	@Test
	public void testNewAppointmentWhenAppointmentDoesNotAlreadyExist() {
		Appointment appointment = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		when(appointmentRepository.findById("1"))
			.thenReturn(null);
		cycleRepairController.newAppointment(appointment);
		InOrder inOrder = inOrder(appointmentRepository, appointmentView);
		inOrder.verify(appointmentRepository).save(appointment);
		inOrder.verify(appointmentView).appointmentAdded(appointment);
	}

	@Test
	public void testNewAppointmentWhenAppointmentAlreadyExists() {
		Appointment appointmentToAdd = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		Appointment existingAppointment = new Appointment("1", "Luigi Bianchi", "City Bike",
				"Flat tyre", "2026-06-11");
		when(appointmentRepository.findById("1"))
			.thenReturn(existingAppointment);
		cycleRepairController.newAppointment(appointmentToAdd);
		verify(appointmentView)
			.showError("Already existing appointment with id 1", existingAppointment);
		verifyNoMoreInteractions(ignoreStubs(appointmentRepository));
	}

	@Test
	public void testDeleteAppointmentWhenAppointmentExists() {
		Appointment appointmentToDelete = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		when(appointmentRepository.findById("1"))
			.thenReturn(appointmentToDelete);
		cycleRepairController.deleteAppointment(appointmentToDelete);
		InOrder inOrder = inOrder(appointmentRepository, appointmentView);
		inOrder.verify(appointmentRepository).delete("1");
		inOrder.verify(appointmentView).appointmentRemoved(appointmentToDelete);
	}

	@Test
	public void testDeleteAppointmentWhenAppointmentDoesNotExist() {
		Appointment appointment = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		when(appointmentRepository.findById("1"))
			.thenReturn(null);
		cycleRepairController.deleteAppointment(appointment);
		verify(appointmentView)
			.showErrorAppointmentNotFound("No existing appointment with id 1", appointment);
		verifyNoMoreInteractions(ignoreStubs(appointmentRepository));
	}
}
