package com.examples.cyclerepair.repository.mongo;

import java.util.List;

import com.examples.cyclerepair.model.Appointment;
import com.examples.cyclerepair.repository.AppointmentRepository;
import com.mongodb.MongoClient;

public class AppointmentMongoRepository implements AppointmentRepository {

	public AppointmentMongoRepository(MongoClient client, String databaseName, String collectionName) {

	}

	@Override
	public List<Appointment> findAll() {
		return null;
	}

	@Override
	public Appointment findById(String id) {
		return null;
	}

	@Override
	public void save(Appointment appointment) {

	}

	@Override
	public void delete(String id) {

	}
}
