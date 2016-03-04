package net.sf.dynamicreports.examples;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;


public class SimpleReportExample {

	public static void main(String[] args) {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test1","root", "root");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return;
		} 

		//a new report
		JasperReportBuilder report = DynamicReports.report();
		report
		.columns(
				Columns.column("Customer Id", "id", DataTypes.integerType()).setHorizontalAlignment(HorizontalAlignment.LEFT),
				Columns.column("First Name", "first_name", DataTypes.stringType()),
				Columns.column("Last Name", "last_name", DataTypes.stringType()),
				Columns.column("Date", "date", DataTypes.dateType()).setHorizontalAlignment(HorizontalAlignment.LEFT))
				//title of the report
				.title(
						Components.text("SimpleReportExample")
						.setHorizontalAlignment(HorizontalAlignment.CENTER))
						.pageFooter(Components.pageXofY())//show page number on the page footer
						.setDataSource("SELECT id, first_name, last_name, date FROM customers", connection);

		try {
			//show the report
			report.show();
			
			//export the report to a pdf file
			String userHome = System.getProperty("user.home");
			report.toPdf(new FileOutputStream(userHome+"/report.pdf"));
		} catch (DRException | FileNotFoundException e) {
			e.printStackTrace();
		} 
	}
}
