package cz.cvut.fit.hrabajak.semestralka.client.gui;

import cz.cvut.fit.hrabajak.semestralka.rest.dto.OrderProductDto;

import javax.swing.table.AbstractTableModel;

public class OrderProductTable extends AbstractTableModel {

	private OrderProductDto[] ls;

	public OrderProductTable(OrderProductDto[] ls) {
		this.ls = ls;
	}

	@Override
	public boolean isCellEditable(int row, int column){
		return false;
	}

	@Override
	public int getRowCount() {
		return this.ls.length;
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
			case 0 :
				return "Product entity id";

			case 1 :
				return "Name";

			case 2 :
				return "Price";

			case 3 :
				return "Quantity";

			case 4 :
				return "Total price";
		}

		return "";
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return String.class;
	}

	@Override
	public Object getValueAt(int row, int column) {
		switch (column) {
			case 0 :
				return Long.toString(this.ls[row].getProduct_entity_id());

			case 1:
				return this.ls[row].getName();

			case 2 :
				return Long.toString(this.ls[row].getPrice());

			case 3 :
				return Long.toString(this.ls[row].getQuantity());

			case 4 :
				return Long.toString(this.ls[row].getTotalPrice());
		}

		return "";
	}

}
