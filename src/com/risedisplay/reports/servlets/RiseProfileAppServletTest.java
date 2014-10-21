package com.risedisplay.reports.servlets;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.risedisplay.reports.util.CsvUtil;

/**
 * Test servlet to insert data locally for testing.
 * 
 *
 */
@SuppressWarnings("serial")
public class RiseProfileAppServletTest extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.getWriter().print("Hello World");
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			
			if(req.getParameter("test") != null){
				String messageString = "ID,Name,Status,Sales Order,Type,Ship Date,Item Description,Item Quantity,Warranty Type,Warranty Start Date,Warranty End Date,Item Notes,Address,Internal ID"
							+ "\n99,Test Customer name,Active,Sales Order #ORD99,Equipment,10/31/2011,TV : External TV Tuner test,1,Parts and Labor,11/1/2011,10/31/2014,Item Note test,test Customer - 123 Street City ST 10000-0000,1"
							+ "\n98,Test2 Customer name,Active,Sales Order #ORD98,Equipment,10/31/2011,TV : External TV Tuner test2,2,Parts and Labor,11/1/2011,10/31/2014,Item Note test2,test2 Customer - 123 Street City ST 10000-1000,2"
							;
				
				System.out.println("Parsing File Data");
				CsvUtil.parseCsv(messageString);
				System.out.println("File Data" + messageString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String a[]) {
		String csvFile = "D:/searchresults.csv";
		BufferedReader br = null;
		String line = "";
		String messageString = "";
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				messageString += line;	 
			}
			
			System.out.println("Parsing File Data");
			CsvUtil.parseCsv(messageString);
			System.out.println("File Data" + messageString);
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		System.out.println("Done");
	}
	
}
