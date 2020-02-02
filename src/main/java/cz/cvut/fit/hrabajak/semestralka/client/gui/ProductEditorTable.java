package cz.cvut.fit.hrabajak.semestralka.client.gui;

import cz.cvut.fit.hrabajak.semestralka.rest.dto.ProductDto;

import javax.swing.table.AbstractTableModel;

public class ProductEditorTable extends AbstractTableModel {

	private ProductDto[] ls;

	public ProductEditorTable(ProductDto[] ls) {
		this.ls = ls;
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
				return "Entity id";

			case 1 :
				return "Name";

			case 2 :
				return "Price";
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
				return Long.toString(this.ls[row].getEntity_id());

			case 1:
				return this.ls[row].getName();

			case 2 :
				return Long.toString(this.ls[row].getPrice());
		}

		return "";
	}
}
