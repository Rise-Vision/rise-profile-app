package com.risedisplay.reports.entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Customer implements Serializable {
	private String customerID;
	private String customerName;
	private String accountStatus;
	private String salesOrder;
	private String type;
	private String shipDate;
	private String itemDescription;
	private String itemQuantity;
	private String warrantyType;
	private String warrantyStartDate;
	private String warrantyEndDate;
	private String itemNotes;
	private String address;
	private String internalID;

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(String salesOrder) {
		this.salesOrder = salesOrder;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getShipDate() {
		return shipDate;
	}

	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	
	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(String itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public String getWarrantyType() {
		return warrantyType;
	}

	public void setWarrantyType(String warrantyType) {
		this.warrantyType = warrantyType;
	}
	
	public String getWarrantyStartDate() {
		return warrantyStartDate;
	}

	public void setWarrantyStartDate(String warrantyStartDate) {
		this.warrantyStartDate = warrantyStartDate;
	}

	public String getWarrantyEndDate() {
		return warrantyEndDate;
	}

	public void setWarrantyEndDate(String warrantyEndDate) {
		this.warrantyEndDate = warrantyEndDate;
	}

	public String getItemNotes() {
		return itemNotes;
	}

	public void setItemNotes(String itemNotes) {
		this.itemNotes = itemNotes;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getInternalID() {
		return internalID;
	}

	public void setInternalID(String internalID) {
		this.internalID = internalID;
	}
	
}
