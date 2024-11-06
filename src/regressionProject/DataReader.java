package regressionProject;

import tech.tablesaw.api.Table;

import java.io.IOException;

import tech.tablesaw.api.DoubleColumn;


public class DataReader {
	
	public DataReader(String filePathString, String columnX, String columnY) {
		try {
			Table data = Table.read().csv(filePathString);
			
			System.out.println(data.structure());
			System.out.println(data.first(5));
			
			DoubleColumn xColumn = data.doubleColumn(columnY);
			DoubleColumn yColumn = data.doubleColumn(columnX);
			
			double[] xData = xColumn.asDoubleArray();
			double[] yData = yColumn.asDoubleArray();
			
		    System.out.println("xData:");
		    for (double x : xData) {
		        System.out.print(x + " ");
		    }
		    System.out.println("\nyData:");
		    for (double y : yData) {
		        System.out.print(y + " ");
		    }
		} catch (IOException e) {
            e.printStackTrace();	
        }
	}
	public static void main(String[] args) {
        DataReader reader = new DataReader("data.csv","date","close");
	}

}
