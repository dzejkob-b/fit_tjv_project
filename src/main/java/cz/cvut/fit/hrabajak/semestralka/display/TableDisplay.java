package cz.cvut.fit.hrabajak.semestralka.display;

import cz.cvut.fit.hrabajak.semestralka.data.DataManager;

public class TableDisplay {

	private DataManager dt;
	private String tableName;

	public TableDisplay(String tableName, DataManager dt) {
		this.tableName = tableName;
		this.dt = dt;
	}

	public void displayToConsole() {

		System.out.println("============================");
		System.out.println(" " + this.tableName);
		System.out.println("============================");

		for (Object row : this.dt.getAllRecordsFromTable(this.tableName)) {

			System.out.println("#~ROW~# " + row.toString());

		}

		System.out.println("//" + this.tableName);

	}

}
