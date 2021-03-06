package cz.cvut.fit.hrabajak.semestralka.client.gui;

import cz.cvut.fit.hrabajak.semestralka.client.consume.ConsumeOrderRecord;
import cz.cvut.fit.hrabajak.semestralka.orm.OrderRecord;
import cz.cvut.fit.hrabajak.semestralka.rest.dto.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
	public JButton bt_deleteProduct;

	private String cStatus;
	private OrderRecordDto cOrderRecord;

	@Autowired
	private ConsumeOrderRecord co;

	public OrderEditor() {
		this.InitializeComponents();
	}

	public OrderRecordDto getCurrentOrderRecord() {
		return this.cOrderRecord;
	}

	public void Destroy() {
		if (this.frame != null) {
			this.frame.dispose();
		}
	}

	public void Initialize() {

		this.frame = new JFrame();
		this.frame.setTitle("BIK-TJV semestralka - Order editor");
		this.frame.setSize(600, 700);
		this.frame.getContentPane().add(this.panel);
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.frame.setResizable(false);

		this.UpdateTable("opened");

		this.frame.toFront();
		this.FrameToParent(this.frame);
		this.frame.setVisible(true);
	}

	private void InitializeComponents() {

		this.table.setRowSelectionAllowed(true);
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.table.getTableHeader().setReorderingAllowed(false);

		this.productTable.setRowSelectionAllowed(true);
		this.productTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.productTable.getTableHeader().setReorderingAllowed(false);

		this.status.addItem(null);
		this.status.addItem(OrderRecord.Status.OPENED);
		this.status.addItem(OrderRecord.Status.PROCESSING);
		this.status.addItem(OrderRecord.Status.FINISHED);
		this.status.addItem(OrderRecord.Status.REVERSAL);

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

		this.bt_deleteProduct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				ActionDeleteProducts();
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
				this.UpdateFields(this.cOrderRecord = this.co.GetOrderRecordByCode(this.code.getText()));
				this.ActionProductsLoad(null);

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Cannot get order record:\n" + ex.getMessage());
				this.cOrderRecord = null;
			}

		}
	}

	private void ActionProductsLoad(OrderRecordProductsDto op) {
		if (op != null) {
			this.productTable.setModel(new OrderProductTable(op.getProductsArray()));

		} else {
			try {
				op = this.co.GetOrderRecordProductsByCode(this.code.getText());

				this.productTable.setModel(new OrderProductTable(op.getProductsArray()));

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Cannot load order record products:\n" + ex.getMessage());
			}
		}
	}

	private void ActionUpdate() {
		try {
			OrderRecordDto o = new OrderRecordDto();

			o.setCode(this.code.getText());
			o.setCustFirstName(this.custFirstName.getText());
			o.setCustSurName(this.custSurName.getText());
			o.setDeliveryAddress(this.deliveryAddress.getText());
			o.setDeliveryCity(this.deliveryCity.getText());

			this.UpdateFields(this.cOrderRecord = this.co.UpdateOrCreateOrderRecord(o));

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Cannot create or update order record:\n" + ex.getMessage());
			this.cOrderRecord = null;
		}

		this.ActionProductsLoad(null);
		this.UpdateTable(null);
	}

	private void ActionUpdateStatus() {
		if (this.code.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "No order record loaded!");

		} else if (this.status.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(null, "No status selected!");

		} else {
			try {
				this.co.SetOrderRecordStatus(this.code.getText(), (OrderRecord.Status) this.status.getSelectedItem());

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Cannot update order record status:\n" + ex.getMessage());
			}

			this.UpdateTable(null);
		}
	}

	private void ActionDelete() {
		try {
			this.co.DeleteOrderRecordByCode(this.code.getText());

			this.UpdateFields(null);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Cannot delete order record:\n" + ex.getMessage());
		}

		this.UpdateTable(null);
	}

	private void ActionDeleteProducts() {
		if (this.cOrderRecord != null && this.productTable.getSelectedRowCount() > 0) {
			try {
				ArrayList<OrderAddProduct> ls = new ArrayList<OrderAddProduct>();

				for (int rowIdx : this.productTable.getSelectedRows()) {
					OrderAddProduct ap = new OrderAddProduct();

					ap.setProduct_id(Long.parseLong((String) this.productTable.getValueAt(rowIdx, 0)));
					ap.setQuantity(Long.parseLong((String) this.productTable.getValueAt(rowIdx, 3)));

					ls.add(ap);
				}

				OrderAddDto d = new OrderAddDto();

				d.setAddProducts(ls);

				this.ActionProductsLoad(this.co.RemoveProducts(this.cOrderRecord.getCode(), d));

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Cannot delete products from order record:\n" + ex.getMessage());
			}

			this.UpdateTable(null);
		}
	}

	public void ActionAddProduct(long productEntity_id, long quantity) {
		if (this.cOrderRecord != null) {
			try {
				ArrayList<OrderAddProduct> ls = new ArrayList<OrderAddProduct>();

				OrderAddProduct ap = new OrderAddProduct();

				ap.setProduct_id(productEntity_id);
				ap.setQuantity(quantity);

				ls.add(ap);

				OrderAddDto d = new OrderAddDto();

				d.setAddProducts(ls);

				this.ActionProductsLoad(this.co.AddProducts(this.cOrderRecord.getCode(), d));

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Cannot add products to order record:\n" + ex.getMessage());
			}

			this.UpdateTable(null);

		} else {
			JOptionPane.showMessageDialog(null, "Order record is not selected!");
		}
	}

	private void UpdateFields(OrderRecordDto o) {
		if (o == null) {
			this.code.setText("");
			this.status.setSelectedItem(null);
			this.custFirstName.setText("");
			this.custSurName.setText("");
			this.deliveryAddress.setText("");
			this.deliveryCity.setText("");

			this.productTable.setModel(new OrderProductTable(new OrderProductDto[0]));
			this.cOrderRecord = null;

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
		gbc.gridy = 3;
		gbc.gridwidth = 16;
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
		gbc.gridheight = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(spacer1, gbc);
		final JPanel spacer2 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 17;
		gbc.gridy = 1;
		gbc.gridheight = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(spacer2, gbc);
		final JPanel spacer3 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 19;
		gbc.gridwidth = 18;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer3, gbc);
		final JPanel spacer4 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 18;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer4, gbc);
		final JLabel label1 = new JLabel();
		label1.setText("Code: ");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label1, gbc);
		code = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 7;
		gbc.gridwidth = 14;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(code, gbc);
		final JLabel label2 = new JLabel();
		label2.setText("First name: ");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 9;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label2, gbc);
		final JLabel label3 = new JLabel();
		label3.setText("Sur name: ");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 10;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label3, gbc);
		final JLabel label4 = new JLabel();
		label4.setText("Delivery address: ");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 11;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label4, gbc);
		final JLabel label5 = new JLabel();
		label5.setText("Delivery city: ");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 12;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label5, gbc);
		custFirstName = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 9;
		gbc.gridwidth = 14;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(custFirstName, gbc);
		custSurName = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 10;
		gbc.gridwidth = 14;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(custSurName, gbc);
		deliveryAddress = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 11;
		gbc.gridwidth = 14;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(deliveryAddress, gbc);
		deliveryCity = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 12;
		gbc.gridwidth = 14;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(deliveryCity, gbc);
		final JPanel spacer5 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 16;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer5, gbc);
		final JLabel label6 = new JLabel();
		label6.setText("Selected order:");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 16;
		gbc.weightx = 1.0;
		panel.add(label6, gbc);
		final JPanel spacer6 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.gridwidth = 16;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer6, gbc);
		final JPanel spacer7 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 15;
		gbc.gridwidth = 16;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer7, gbc);
		final JScrollPane scrollPane2 = new JScrollPane();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 16;
		gbc.gridwidth = 16;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane2, gbc);
		productTable = new JTable();
		scrollPane2.setViewportView(productTable);
		final JLabel label7 = new JLabel();
		label7.setText("Status: ");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 8;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label7, gbc);
		status = new JComboBox();
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 8;
		gbc.gridwidth = 13;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(status, gbc);
		final JLabel label8 = new JLabel();
		label8.setText("List orders:");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label8, gbc);
		final JPanel spacer8 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 13;
		gbc.gridwidth = 16;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer8, gbc);
		final JPanel spacer9 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 17;
		gbc.gridwidth = 16;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer9, gbc);
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.gridwidth = 14;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(panel1, gbc);
		bt_opened = new JButton();
		bt_opened.setPreferredSize(new Dimension(100, 30));
		bt_opened.setText("Opened");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel1.add(bt_opened, gbc);
		bt_processing = new JButton();
		bt_processing.setPreferredSize(new Dimension(100, 30));
		bt_processing.setText("Processing");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel1.add(bt_processing, gbc);
		bt_reversal = new JButton();
		bt_reversal.setPreferredSize(new Dimension(100, 30));
		bt_reversal.setText("Reversal");
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 0;
		panel1.add(bt_reversal, gbc);
		bt_finished = new JButton();
		bt_finished.setPreferredSize(new Dimension(100, 30));
		bt_finished.setText("Finished");
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 0;
		panel1.add(bt_finished, gbc);
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 4;
		gbc.gridy = 14;
		gbc.gridwidth = 13;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(panel2, gbc);
		bt_delete = new JButton();
		bt_delete.setPreferredSize(new Dimension(100, 30));
		bt_delete.setText("Delete");
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 0;
		panel2.add(bt_delete, gbc);
		bt_load = new JButton();
		bt_load.setPreferredSize(new Dimension(100, 30));
		bt_load.setText("Load");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel2.add(bt_load, gbc);
		bt_update = new JButton();
		bt_update.setPreferredSize(new Dimension(100, 30));
		bt_update.setText("Create, update");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		panel2.add(bt_update, gbc);
		bt_clean = new JButton();
		bt_clean.setPreferredSize(new Dimension(100, 30));
		bt_clean.setText("Clean form");
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 0;
		panel2.add(bt_clean, gbc);
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 16;
		gbc.gridy = 18;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(panel3, gbc);
		bt_deleteProduct = new JButton();
		bt_deleteProduct.setPreferredSize(new Dimension(120, 30));
		bt_deleteProduct.setText("Delete products");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel3.add(bt_deleteProduct, gbc);
		final JPanel panel4 = new JPanel();
		panel4.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 16;
		gbc.gridy = 8;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(panel4, gbc);
		bt_status = new JButton();
		bt_status.setPreferredSize(new Dimension(100, 30));
		bt_status.setText("Update status");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel4.add(bt_status, gbc);
		final JPanel spacer10 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 16;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer10, gbc);
		final JPanel spacer11 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 7;
		gbc.gridheight = 6;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(spacer11, gbc);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() { return panel; }

}
