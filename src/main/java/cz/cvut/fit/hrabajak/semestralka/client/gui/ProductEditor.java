package cz.cvut.fit.hrabajak.semestralka.client.gui;

import cz.cvut.fit.hrabajak.semestralka.client.consume.ConsumeProduct;
import cz.cvut.fit.hrabajak.semestralka.rest.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProductEditor extends FormBasic {

	public JFrame frame;
	public JPanel panel;
	public JTextField name;
	public JTextField entity_id;
	public JTextField price;
	public JButton bt_load;
	public JButton bt_update;
	public JButton bt_clean;
	public JButton bt_delete;
	public JTable table;
	public JButton bt_prev;
	public JButton bt_next;
	public JTextField quantity;
	public JButton bt_order;

	private int cPage = 0;
	private ProductDto cProduct = null;

	@Autowired
	private ConsumeProduct cp;
	@Autowired
	private OrderEditor oe;

	public ProductEditor() {
		this.InitializeComponents();
	}

	public void Destroy() {
		if (this.frame != null) {
			this.frame.dispose();
		}
	}

	public void Initialize() {

		this.frame = new JFrame();
		this.frame.setTitle("BIK-TJV semestralka - Product editor");
		this.frame.setSize(600, 400);
		this.frame.getContentPane().add(this.panel);
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.frame.setResizable(false);

		this.UpdateTable(0);

		this.frame.toFront();
		this.FrameToParent(this.frame);
		this.frame.setVisible(true);
	}

	private void InitializeComponents() {

		this.table.setRowSelectionAllowed(true);
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.table.getTableHeader().setReorderingAllowed(false);

		this.bt_load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				ActionLoad();
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

		this.bt_order.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				ActionProductToOrder();
			}
		});

		this.bt_prev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				UpdateTable(-1);
			}
		});

		this.bt_next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				UpdateTable(1);
			}
		});

		this.table.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mouseClicked(e);

				if (e.getClickCount() == 2 && table.getSelectedRowCount() > 0) {

					entity_id.setText((String) table.getValueAt(table.getSelectedRow(), 0));
					ActionLoad();

				}

			}
		});
	}

	private void ActionLoad() {
		if (entity_id.getText().trim().isEmpty()) {
			this.UpdateFields(null);

		} else {
			try {
				this.UpdateFields(this.cProduct = this.cp.GetProductById(Long.parseLong(this.entity_id.getText())));

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Cannot get product:\n" + ex.getMessage());
				this.cProduct = null;
			}
		}
	}

	private void ActionUpdate() {
		try {

			ProductDto p = new ProductDto();

			if (!this.entity_id.getText().trim().isEmpty()) {
				p.setEntity_id(Long.parseLong(this.entity_id.getText()));
			}

			p.setName(this.name.getText());
			p.setPrice(Long.parseLong(this.price.getText()));

			this.UpdateFields(this.cProduct = cp.UpdateOrCreateProduct(p));
			this.UpdateTable(0);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Cannot create or update product:\n" + ex.getMessage());
			this.cProduct = null;
		}
	}

	private void ActionDelete() {
		try {
			this.cp.DeleteProductById(Long.parseLong(entity_id.getText()));

			this.UpdateFields(null);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Cannot delete product:\n" + ex.getMessage());
		}

		this.UpdateTable(0);
	}

	private void ActionProductToOrder() {
		if (this.cProduct == null) {
			JOptionPane.showMessageDialog(null, "Product is not selected!");

		} else {
			long quantity = 0;

			try {
				quantity = Long.parseLong(this.quantity.getText());
			} catch (Exception ex) {
				// ...
			}

			this.oe.ActionAddProduct(this.cProduct.getEntity_id(), quantity);
		}
	}

	private void UpdateFields(ProductDto p) {
		if (p == null) {
			this.entity_id.setText("");
			this.name.setText("");
			this.price.setText("");

			this.cProduct = null;

		} else {
			this.entity_id.setText(Long.toString(p.getEntity_id()));
			this.name.setText(p.getName());
			this.price.setText(Long.toString(p.getPrice()));
		}
	}

	private void UpdateTable(int step) {

		int nPage = this.cPage + step;

		if (nPage < 0) {
			nPage = 0;
		}

		ProductDto[] ls = null;

		try {
			ls = this.cp.GetProducts(nPage);
			this.cPage = nPage;
			this.table.setModel(new ProductEditorTable(ls));

		} catch (Exception ex) {
			ls = new ProductDto[0];

			if (nPage > 0 && nPage == this.cPage) {
				// not found - prazdny seznam - krok na predchozi stranku

				this.UpdateTable(-1);

			} else if (this.cPage == 0) {
				this.table.setModel(new ProductEditorTable(ls));
			}
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
		final JLabel label1 = new JLabel();
		label1.setText("Name:");
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label1, gbc);
		name = new JTextField();
		name.setColumns(20);
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 2;
		gbc.weightx = 2.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(name, gbc);
		final JLabel label2 = new JLabel();
		label2.setText("Price:");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label2, gbc);
		final JLabel label3 = new JLabel();
		label3.setText("Entity id: ");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		panel.add(label3, gbc);
		entity_id = new JTextField();
		entity_id.setColumns(10);
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.weightx = 2.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(entity_id, gbc);
		price = new JTextField();
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 3;
		gbc.weightx = 2.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(price, gbc);
		final JPanel spacer1 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer1, gbc);
		final JScrollPane scrollPane1 = new JScrollPane();
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 5;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(scrollPane1, gbc);
		table = new JTable();
		table.setAutoCreateRowSorter(false);
		scrollPane1.setViewportView(table);
		final JPanel spacer2 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 6;
		gbc.gridy = 1;
		gbc.gridheight = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(spacer2, gbc);
		final JPanel spacer3 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(spacer3, gbc);
		final JPanel spacer4 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 7;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer4, gbc);
		final JPanel spacer5 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 7;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel.add(spacer5, gbc);
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
		gbc = new GridBagConstraints();
		gbc.gridx = 5;
		gbc.gridy = 6;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(panel1, gbc);
		bt_prev = new JButton();
		bt_prev.setPreferredSize(new Dimension(100, 30));
		bt_prev.setText("<= prev");
		panel1.add(bt_prev);
		bt_next = new JButton();
		bt_next.setPreferredSize(new Dimension(100, 30));
		bt_next.setText("next =>");
		panel1.add(bt_next);
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
		gbc = new GridBagConstraints();
		gbc.gridx = 5;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(panel2, gbc);
		bt_load = new JButton();
		bt_load.setHorizontalAlignment(0);
		bt_load.setPreferredSize(new Dimension(110, 30));
		bt_load.setText("Load");
		panel2.add(bt_load);
		bt_clean = new JButton();
		bt_clean.setPreferredSize(new Dimension(110, 30));
		bt_clean.setText("Clean form");
		panel2.add(bt_clean);
		final JPanel spacer6 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 4;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(spacer6, gbc);
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
		gbc = new GridBagConstraints();
		gbc.gridx = 5;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(panel3, gbc);
		bt_update = new JButton();
		bt_update.setPreferredSize(new Dimension(110, 30));
		bt_update.setText("Create, update");
		panel3.add(bt_update);
		bt_delete = new JButton();
		bt_delete.setPreferredSize(new Dimension(110, 30));
		bt_delete.setText("Delete");
		panel3.add(bt_delete);
		final JPanel panel4 = new JPanel();
		panel4.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
		gbc = new GridBagConstraints();
		gbc.gridx = 5;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(panel4, gbc);
		final JLabel label4 = new JLabel();
		label4.setText("Quantity: ");
		panel4.add(label4);
		quantity = new JTextField();
		quantity.setPreferredSize(new Dimension(60, 30));
		panel4.add(quantity);
		bt_order = new JButton();
		bt_order.setPreferredSize(new Dimension(80, 30));
		bt_order.setText("To order");
		panel4.add(bt_order);
		final JPanel spacer7 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridheight = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(spacer7, gbc);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() { return panel; }

}
