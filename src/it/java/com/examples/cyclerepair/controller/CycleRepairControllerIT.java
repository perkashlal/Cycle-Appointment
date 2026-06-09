package com.examples.cyclerepair.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.examples.cyclerepair.model.Appointment;
import com.examples.cyclerepair.repository.AppointmentRepository;
import com.examples.cyclerepair.repository.mongo.AppointmentMongoRepository;
import com.examples.cyclerepair.view.AppointmentView;
import com.mongodb.MongoClient;

public class CycleRepairControllerIT {

	@Mock
	private AppointmentView appointmentView;

	private AppointmentRepository appointmentRepository;

	private CycleRepairController cycleRepairController;

	private static final String CYCLE_REPAIR_DB_NAME = "cycle-repair";
	private static final String APPOINTMENT_COLLECTION_NAME = "appointment";

	private AutoCloseable closeable;

	private static int mongoPort =
		Integer.parseInt(System.getProperty("mongo.port", "27017"));

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		appointmentRepository =
			new AppointmentMongoRepository(new MongoClient("localhost", mongoPort),
					CYCLE_REPAIR_DB_NAME, APPOINTMENT_COLLECTION_NAME);
		// explicit empty the database through the repository
		for (Appointment appointment : appointmentRepository.findAll()) {
			appointmentRepository.delete(appointment.getId());
		}
		cycleRepairController = new CycleRepairController(appointmentView, appointmentRepository);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllAppointments() {
		Appointment appointment = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		appointmentRepository.save(appointment);
		cycleRepairController.allAppointments();
		verify(appointmentView)
			.showAllAppointments(asList(appointment));
	}

	@Test
	public void testNewAppointment() {
		Appointment appointment = new Appointment("1", "Mario Rossi", "Road Bike",
				"Brake adjustment", "2026-06-10");
		cycleRepairController.newAppointment(appointment);
		verify(appointmentView).appointmentAdded(appointment);
	}

}
