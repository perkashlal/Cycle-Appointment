package com.examples.cyclerepair.view.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.examples.cyclerepair.controller.CycleRepairController;
import com.examples.cyclerepair.model.Appointment;
import com.examples.cyclerepair.view.AppointmentView;

public class AppointmentSwingView extends JFrame implements AppointmentView {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtCustomerName;
	private JTextField txtCycleModel;
	private JTextField txtRepairIssue;
	private JTextField txtAppointmentDate;
	private JButton btnAdd;
	private JList<Appointment> listAppointments;
	private JScrollPane scrollPane;
	private JButton btnDeleteSelected;
	private JLabel lblErrorMessage;

	private DefaultListModel<Appointment> listAppointmentsModel;

	private transient CycleRepairController cycleRepairController;

	public void setCycleRepairController(CycleRepairController cycleRepairController) {
		this.cycleRepairController = cycleRepairController;
	}

	public AppointmentSwingView() {
		setTitle("Appointment View");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		JLabel lblId = new JLabel("id");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.anchor = GridBagConstraints.EAST;
		gbc_lblId.gridx = 0;
		gbc_lblId.gridy = 0;
		contentPane.add(lblId, gbc_lblId);

		txtId = new JTextField();
		KeyAdapter btnAddEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAdd.setEnabled(
					!txtId.getText().trim().isEmpty() &&
					!txtCustomerName.getText().trim().isEmpty() &&
					!txtCycleModel.getText().trim().isEmpty() &&
					!txtRepairIssue.getText().trim().isEmpty() &&
					!txtAppointmentDate.getText().trim().isEmpty()
				);
			}
		};
		txtId.addKeyListener(btnAddEnabler);
		txtId.setName("idTextBox");
		GridBagConstraints gbc_idTextField = new GridBagConstraints();
		gbc_idTextField.insets = new Insets(0, 0, 5, 0);
		gbc_idTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_idTextField.gridx = 1;
		gbc_idTextField.gridy = 0;
		contentPane.add(txtId, gbc_idTextField);
		txtId.setColumns(10);

		JLabel lblCustomerName = new JLabel("customer name");
		GridBagConstraints gbc_lblCustomerName = new GridBagConstraints();
		gbc_lblCustomerName.anchor = GridBagConstraints.EAST;
		gbc_lblCustomerName.insets = new Insets(0, 0, 5, 5);
		gbc_lblCustomerName.gridx = 0;
		gbc_lblCustomerName.gridy = 1;
		contentPane.add(lblCustomerName, gbc_lblCustomerName);

		txtCustomerName = new JTextField();
		txtCustomerName.addKeyListener(btnAddEnabler);
		txtCustomerName.setName("customerNameTextBox");
		GridBagConstraints gbc_customerNameTextField = new GridBagConstraints();
		gbc_customerNameTextField.insets = new Insets(0, 0, 5, 0);
		gbc_customerNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_customerNameTextField.gridx = 1;
		gbc_customerNameTextField.gridy = 1;
		contentPane.add(txtCustomerName, gbc_customerNameTextField);
		txtCustomerName.setColumns(10);

		JLabel lblCycleModel = new JLabel("cycle model");
		GridBagConstraints gbc_lblCycleModel = new GridBagConstraints();
		gbc_lblCycleModel.anchor = GridBagConstraints.EAST;
		gbc_lblCycleModel.insets = new Insets(0, 0, 5, 5);
		gbc_lblCycleModel.gridx = 0;
		gbc_lblCycleModel.gridy = 2;
		contentPane.add(lblCycleModel, gbc_lblCycleModel);

		txtCycleModel = new JTextField();
		txtCycleModel.addKeyListener(btnAddEnabler);
		txtCycleModel.setName("cycleModelTextBox");
		GridBagConstraints gbc_cycleModelTextField = new GridBagConstraints();
		gbc_cycleModelTextField.insets = new Insets(0, 0, 5, 0);
		gbc_cycleModelTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_cycleModelTextField.gridx = 1;
		gbc_cycleModelTextField.gridy = 2;
		contentPane.add(txtCycleModel, gbc_cycleModelTextField);
		txtCycleModel.setColumns(10);

		JLabel lblRepairIssue = new JLabel("repair issue");
		GridBagConstraints gbc_lblRepairIssue = new GridBagConstraints();
		gbc_lblRepairIssue.anchor = GridBagConstraints.EAST;
		gbc_lblRepairIssue.insets = new Insets(0, 0, 5, 5);
		gbc_lblRepairIssue.gridx = 0;
		gbc_lblRepairIssue.gridy = 3;
		contentPane.add(lblRepairIssue, gbc_lblRepairIssue);

		txtRepairIssue = new JTextField();
		txtRepairIssue.addKeyListener(btnAddEnabler);
		txtRepairIssue.setName("repairIssueTextBox");
		GridBagConstraints gbc_repairIssueTextField = new GridBagConstraints();
		gbc_repairIssueTextField.insets = new Insets(0, 0, 5, 0);
		gbc_repairIssueTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_repairIssueTextField.gridx = 1;
		gbc_repairIssueTextField.gridy = 3;
		contentPane.add(txtRepairIssue, gbc_repairIssueTextField);
		txtRepairIssue.setColumns(10);

		JLabel lblAppointmentDate = new JLabel("appointment date");
		GridBagConstraints gbc_lblAppointmentDate = new GridBagConstraints();
		gbc_lblAppointmentDate.anchor = GridBagConstraints.EAST;
		gbc_lblAppointmentDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblAppointmentDate.gridx = 0;
		gbc_lblAppointmentDate.gridy = 4;
		contentPane.add(lblAppointmentDate, gbc_lblAppointmentDate);

		txtAppointmentDate = new JTextField();
		txtAppointmentDate.addKeyListener(btnAddEnabler);
		txtAppointmentDate.setName("appointmentDateTextBox");
		GridBagConstraints gbc_appointmentDateTextField = new GridBagConstraints();
		gbc_appointmentDateTextField.insets = new Insets(0, 0, 5, 0);
		gbc_appointmentDateTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_appointmentDateTextField.gridx = 1;
		gbc_appointmentDateTextField.gridy = 4;
		contentPane.add(txtAppointmentDate, gbc_appointmentDateTextField);
		txtAppointmentDate.setColumns(10);

		btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdd.gridwidth = 2;
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 5;
		contentPane.add(btnAdd, gbc_btnAdd);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 6;
		contentPane.add(scrollPane, gbc_scrollPane);

		listAppointmentsModel = new DefaultListModel<>();
		listAppointments = new JList<>(listAppointmentsModel);
		listAppointments.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				Appointment appointment = (Appointment) value;
				return super.getListCellRendererComponent(list,
					getDisplayString(appointment),
					index, isSelected, cellHasFocus);
			}
		});
		listAppointments.setName("appointmentList");
		scrollPane.setViewportView(listAppointments);

		btnDeleteSelected = new JButton("Delete Selected");
		btnDeleteSelected.setEnabled(false);
		GridBagConstraints gbc_btnDeleteSelected = new GridBagConstraints();
		gbc_btnDeleteSelected.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeleteSelected.gridwidth = 2;
		gbc_btnDeleteSelected.gridx = 0;
		gbc_btnDeleteSelected.gridy = 7;
		contentPane.add(btnDeleteSelected, gbc_btnDeleteSelected);

		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setForeground(Color.RED);
		lblErrorMessage.setName("errorMessageLabel");
		GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
		gbc_lblErrorMessage.gridwidth = 2;
		gbc_lblErrorMessage.insets = new Insets(0, 0, 0, 5);
		gbc_lblErrorMessage.gridx = 0;
		gbc_lblErrorMessage.gridy = 8;
		contentPane.add(lblErrorMessage, gbc_lblErrorMessage);
	}

	@Override
	public void showAllAppointments(List<Appointment> appointments) {
		appointments.stream().forEach(listAppointmentsModel::addElement);
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

	private String getDisplayString(Appointment appointment) {
		return appointment.getId() + " - " + appointment.getCustomerName() + " - "
				+ appointment.getCycleModel() + " - " + appointment.getRepairIssue() + " - "
				+ appointment.getAppointmentDate();
	}
}
