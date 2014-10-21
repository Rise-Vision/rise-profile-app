package com.risedisplay.reports.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.risedisplay.reports.entities.Customer;

@SuppressWarnings("serial")
public class ExportCustomerRecordsServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doPost(req, resp);
	}

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			StringBuffer data = new StringBuffer("ID,Name,Status,Sales Order,Type,Ship Date,Item Description,Item Quantity,Warranty Type,Warranty Start Date,Warranty End Date,Item Notes,Address,Internal ID");
			
			if(req.getSession().getAttribute("customers") != null) {
				List<Customer> list = (List<Customer>) req.getSession().getAttribute("customers");
				for (Customer customer : list) {
					data.append("\n");
					data.append(customer.getCustomerID()+",");
					data.append(customer.getCustomerName()+",");
					data.append(customer.getAccountStatus()+",");
					data.append(customer.getSalesOrder()+",");
					data.append(customer.getType()+",");
					data.append(customer.getShipDate()+",");
					data.append(customer.getItemDescription()+",");
					data.append(customer.getItemQuantity()+",");
					data.append(customer.getWarrantyType()+",");
					data.append(customer.getWarrantyStartDate()+",");
					data.append(customer.getWarrantyEndDate()+",");
					data.append(customer.getItemNotes()+",");
					data.append(customer.getAddress()+",");
					data.append(customer.getInternalID());
				}
			}
			
			resp.setHeader("Content-Type", "text/csv");
		    resp.setHeader("Content-Disposition", "attachment;filename=\"searchresults.csv\"");
			resp.getWriter().print(data.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
