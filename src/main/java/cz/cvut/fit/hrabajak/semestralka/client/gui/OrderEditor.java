package cz.cvut.fit.hrabajak.semestralka.client.gui;

import cz.cvut.fit.hrabajak.semestralka.client.consume.ConsumeOrderRecord;
import cz.cvut.fit.hrabajak.semestralka.orm.OrderRecord;
import cz.cvut.fit.hrabajak.semestralka.rest.dto.OrderRecordDto;
import cz.cvut.fit.hrabajak.semestralka.rest.dto.OrderRecordSimpleDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OrderEditor extends FormBasic {

	public JFrame frame;
	public JButton bt_opened;
	public JButton bt_processing;
	public JButton bt_finished;
	public JButton bt_reversal;
	public JTable table;
	public JTextField code;
	public JTextField custFirstName;
	public JTextField custSurName;
	public JTextField deliveryAddress;
	public JTextField deliveryCity;
	public JPanel panel;
	public JTable productTable;
	public JComboBox status;
	public JButton bt_status;
	public JButton bt_load;
	public JButton bt_update;
	public JButton bt_clean;
	public JButton bt_delete;

	private String cStatus;

	@Autowired
	private ConsumeOrderRecord co;

	public OrderEditor() {
	}

	public void Initialize() {
		this.frame = new JFrame();
		this.frame.setTitle("Order editor");
		this.frame.setSize(600, 700);
		this.frame.getContentPane().add(this.panel);
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.frame.setResizable(false);

		this.table.setRowSelectionAllowed(true);
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		this.status.addItem(null);
		this.status.addItem(OrderRecord.Status.OPENED);
		this.status.addItem(OrderRecord.Status.PROCESSING);
		this.status.addItem(OrderRecord.Status.FINISHED);
		this.status.addItem(OrderRecord.Status.REVERSAL);

		this.UpdateTable("opened");

		this.frame.toFront();
		this.FrameToParent(this.frame);
		this.frame.setVisible(true);

		this.bt_opened.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				UpdateTable("opened");
			}
		});
		this.bt_processing.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				UpdateTable("processing");
			}
		});
		this.bt_finished.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				UpdateTable("finished");
			}
		});
		this.bt_reversal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				UpdateTable("reversal");
			}
		});

		this.bt_load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				ActionLoad();
			}
		});

		this.bt_status.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				ActionUpdateStatus();
			}
		});

		this.bt_update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				ActionUpdate();
			}
		});

		this.bt_clean.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				UpdateFields(null);
			}
		});

		this.bt_delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				ActionDelete();
			}
		});

		this.table.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mouseClicked(e);

				if (e.getClickCount() == 2 && table.getSelectedRowCount() > 0) {

					code.setText((String) table.getValueAt(table.getSelectedRow(), 0));
					ActionLoad();

				}

			}
		});

	}

	private void ActionLoad() {
		if (this.code.getText().trim().isEmpty()) {
			this.UpdateFields(null);

		} else {
			try {
				this.UpdateFields(co.GetOrderRecordByCode(this.code.getText()));

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Cannot get order record: " + ex.getMessage());
			}

		}
	}

	private void ActionUpdate() {
	}

	private void ActionUpdateStatus() {
		if (this.code.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "No order record loaded!");

		} else if (this.status.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(null, "No status selected!");

		} else {
			try {
				co.SetOrderRecordStatus(this.code.getText(), (OrderRecord.Status)this.status.getSelectedItem());

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Cannot update order record status: " + ex.getMessage());
			}

			this.UpdateTable(null);
		}
	}

	private void ActionDelete() {
		try {
			co.DeleteOrderRecordByCode(this.code.getText());

			this.UpdateFields(null);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Cannot delete order record: " + ex.getMessage());
		}

		this.UpdateTable(null);
	}

	private void UpdateFields(OrderRecordDto o) {
		if (o == null) {
			this.code.setText("");
			this.status.setSelectedItem(null);
			this.custFirstName.setText("");
			this.custSurName.setText("");
			this.deliveryAddress.setText("");
			this.deliveryCity.setText("");

		} else {
			this.code.setText(o.getCode());
			this.status.setSelectedItem(o.getStatus());
			this.custFirstName.setText(o.getCustFirstName());
			this.custSurName.setText(o.getCustSurName());
			this.deliveryAddress.setText(o.getDeliveryAddress());
			this.deliveryCity.setText(o.getDeliveryCity());
		}
	}

	private void UpdateTable(String status) {

		if (status == null) {
			status = this.cStatus;
		}

		OrderRecordSimpleDto[] ls = null;

		try {
			ls = this.co.GetOrderRecords(status);
			this.table.setModel(new OrderRecordSimpleTable(ls));
			this.cStatus = status;

		} catch (Exception ex) {
			ls = new OrderRecordSimpleDto[0];
			this.table.setModel(new OrderRecordSimpleTable(ls));
		}

	}

	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		final JScrollPane scrollPane1 = new JScrollPane();
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 11;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane1, gbc);
		table = new JTable();
		scrollPane1.setViewportView(table);
		final JPanel spacer1 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(spacer1, gbc);
		final JPanel spacer2 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 12;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(spacer2, gbc);
		final JPanel spacer3 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 16;
		gbc.gridwidth = 13;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer3, gbc);
		final JPanel spacer4 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 13;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer4, gbc);
		final JLabel label1 = new JLabel();
		label1.setText("Code: ");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label1, gbc);
		code = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 6;
		gbc.gridwidth = 10;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(code, gbc);
		final JLabel label2 = new JLabel();
		label2.setText("First name: ");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 8;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label2, gbc);
		final JLabel label3 = new JLabel();
		label3.setText("Sur name: ");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 9;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label3, gbc);
		final JLabel label4 = new JLabel();
		label4.setText("Delivery address: ");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 10;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label4, gbc);
		final JLabel label5 = new JLabel();
		label5.setText("Delivery city: ");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 11;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label5, gbc);
		custFirstName = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 8;
		gbc.gridwidth = 10;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(custFirstName, gbc);
		custSurName = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 9;
		gbc.gridwidth = 10;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(custSurName, gbc);
		deliveryAddress = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 10;
		gbc.gridwidth = 10;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(deliveryAddress, gbc);
		deliveryCity = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 11;
		gbc.gridwidth = 10;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(deliveryCity, gbc);
		final JPanel spacer5 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 9;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer5, gbc);
		final JLabel label6 = new JLabel();
		label6.setText("Selected order:");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 11;
		gbc.weightx = 1.0;
		panel.add(label6, gbc);
		final JPanel spacer6 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 11;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer6, gbc);
		final JPanel spacer7 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 14;
		gbc.gridwidth = 11;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer7, gbc);
		final JScrollPane scrollPane2 = new JScrollPane();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 15;
		gbc.gridwidth = 11;
		gbc.weightx = 1.0;
		gbc.weighty = 2.0;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane2, gbc);
		productTable = new JTable();
		scrollPane2.setViewportView(productTable);
		final JLabel label7 = new JLabel();
		label7.setText("Status: ");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label7, gbc);
		status = new JComboBox();
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 7;
		gbc.gridwidth = 9;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(status, gbc);
		bt_status = new JButton();
		bt_status.setText("Update status");
		gbc = new GridBagConstraints();
		gbc.gridx = 11;
		gbc.gridy = 7;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(bt_status, gbc);
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 6;
		gbc.gridy = 13;
		gbc.gridwidth = 6;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(panel1, gbc);
		bt_load = new JButton();
		bt_load.setText("Load");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel1.add(bt_load, gbc);
		bt_update = new JButton();
		bt_update.setText("Create, update");
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel1.add(bt_update, gbc);
		bt_clean = new JButton();
		bt_clean.setText("Clean form");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel1.add(bt_clean, gbc);
		bt_delete = new JButton();
		bt_delete.setText("Delete");
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel1.add(bt_delete, gbc);
		final JLabel label8 = new JLabel();
		label8.setText("List orders:");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label8, gbc);
		bt_opened = new JButton();
		bt_opened.setText("Opened");
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(bt_opened, gbc);
		bt_processing = new JButton();
		bt_processing.setText("Processing");
		gbc = new GridBagConstraints();
		gbc.gridx = 5;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(bt_processing, gbc);
		bt_finished = new JButton();
		bt_finished.setText("Finished");
		gbc = new GridBagConstraints();
		gbc.gridx = 6;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(bt_finished, gbc);
		bt_reversal = new JButton();
		bt_reversal.setText("Reversal");
		gbc = new GridBagConstraints();
		gbc.gridx = 7;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(bt_reversal, gbc);
		final JPanel spacer8 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 12;
		gbc.gridwidth = 11;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer8, gbc);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() { return panel; }

}
