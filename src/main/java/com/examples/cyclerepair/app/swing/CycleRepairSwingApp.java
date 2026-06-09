package com.examples.cyclerepair.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.examples.cyclerepair.controller.CycleRepairController;
import com.examples.cyclerepair.repository.mongo.AppointmentMongoRepository;
import com.examples.cyclerepair.view.swing.AppointmentSwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class CycleRepairSwingApp implements Callable<Void> {

	@Option(names = { "--mongo-host" }, description = "MongoDB host address")
	private String mongoHost = "localhost";

	@Option(names = { "--mongo-port" }, description = "MongoDB host port")
	private int mongoPort = 27017;

	@Option(names = { "--db-name" }, description = "Database name")
	private String databaseName = "cycle-repair";

	@Option(names = { "--db-collection" }, description = "Collection name")
	private String collectionName = "appointment";

	public static void main(String[] args) {
		new CommandLine(new CycleRepairSwingApp()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				AppointmentMongoRepository appointmentRepository = new AppointmentMongoRepository(
						new MongoClient(new ServerAddress(mongoHost, mongoPort)), databaseName, collectionName);
				AppointmentSwingView appointmentView = new AppointmentSwingView();
				CycleRepairController cycleRepairController =
						new CycleRepairController(appointmentView, appointmentRepository);
				appointmentView.setCycleRepairController(cycleRepairController);
				appointmentView.setVisible(true);
				cycleRepairController.allAppointments();
			} catch (Exception e) {
				Logger.getLogger(getClass().getName())
					.log(Level.SEVERE, "Exception", e);
			}
		});
		return null;
	}

}
