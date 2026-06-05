package com.examples.cyclerepair.model;

public class Appointment {

	private String id;
	private String customerName;
	private String cycleModel;
	private String repairIssue;
	private String appointmentDate;

	public Appointment(String id, String customerName, String cycleModel, String repairIssue,
			String appointmentDate) {
		this.id = id;
		this.customerName = customerName;
		this.cycleModel = cycleModel;
		this.repairIssue = repairIssue;
		this.appointmentDate = appointmentDate;
	}

	@Override
	public String toString() {
		return "Appointment [id=" + id + ", customerName=" + customerName + ", cycleModel="
				+ cycleModel + ", repairIssue=" + repairIssue + ", appointmentDate="
				+ appointmentDate + "]";
	}
}
