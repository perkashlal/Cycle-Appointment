package com.examples.cyclerepair.model;

import java.util.Objects;

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

	public String getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, customerName, cycleModel, repairIssue, appointmentDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Appointment other = (Appointment) obj;
		return Objects.equals(id, other.id)
				&& Objects.equals(customerName, other.customerName)
				&& Objects.equals(cycleModel, other.cycleModel)
				&& Objects.equals(repairIssue, other.repairIssue)
				&& Objects.equals(appointmentDate, other.appointmentDate);
	}

	@Override
	public String toString() {
		return "Appointment [id=" + id + ", customerName=" + customerName + ", cycleModel="
				+ cycleModel + ", repairIssue=" + repairIssue + ", appointmentDate="
				+ appointmentDate + "]";
	}
}
