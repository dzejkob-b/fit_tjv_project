package cz.cvut.fit.hrabajak.semestralka.client.gui;

import cz.cvut.fit.hrabajak.semestralka.rest.dto.OrderRecordSimpleDto;

import javax.swing.table.AbstractTableModel;

public class OrderRecordSimpleTable extends AbstractTableModel {

	private OrderRecordSimpleDto[] ls;

	public OrderRecordSimpleTable(OrderRecordSimpleDto[] ls) {
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
		return 3;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
			case 0 :
				return "Code";

			case 1 :
				return "Status";

			case 2 :
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
				return this.ls[row].getCode();

			case 1:
				return this.ls[row].getStatus().toString();

			case 2 :
				return Long.toString(this.ls[row].getTotalPrice());
		}

		return "";
	}

}
