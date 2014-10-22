<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Customer Records</title>
<script type="text/javascript"
	src="http://dev.sencha.com/deploy/ext-4.0.0/ext-all.js"></script>
<link
	href="http://dev.sencha.com/deploy/ext-4.0.0//resources/css/ext-all.css"
	rel="stylesheet" type="text/css" />
<style type="text/css">
	td div.x-grid-cell-inner {
	white-space: normal;
	}
</style>	
<script type="text/javascript">
	Ext.onReady(function() {
		var params = Ext.urlDecode(location.search.substring(1));
		var customerImporterUrl = 'importCustomerRecords';
		if(typeof params.customerid !== 'undefined'){
			customerImporterUrl = 'importCustomerRecords?customerid='+params.customerid;
		}
		
		var itemsPerPage = 200;
		
		Ext.define('Customer', {
			extend : 'Ext.data.Model',
			fields : [ {
				name : 'customerID',
				type : 'string',
			}, {
				name : 'customerName',
				type : 'string'
			}, {
				name : 'accountStatus',
				type : 'string',
			}, {
				name : 'salesOrder',
				type : 'string',
			}, {
				name : 'type',
				type : 'string',
			}, {
				name : 'shipDate',
				type : 'string',
			}, {
				name : 'itemDescription',
				type : 'string',
			},{
				name : 'itemQuantity',
				type : 'int',
			},{
				name : 'warrantyType',
				type : 'string',
			}, {				
				name : 'warrantyStartDate',
				type : 'string',
				//type : 'date',
				//renderer: Ext.util.Format.dateRenderer('m/d/Y')
			},{
				name : 'warrantyEndDate',
				type : 'string',
				//type : 'date',
			},{
				name : 'itemNotes',
				type : 'string',
			},{
				name : 'address',
				type : 'string',
			}, {				
				name : 'internalID',
				type : 'string',
			}

			]
		});
		
		var custStore = Ext.create('Ext.data.Store', {
			model : 'Customer',
			pageSize : itemsPerPage,
			proxy : {
				type : 'ajax',
				//url : 'importCustomerRecords',
				url : customerImporterUrl,
				startParam : 'startIndex',
				reader : {
					type : 'json',
					root : 'data',
					totalProperty : 'total',
				}
			}
		});
		
		Ext.create('Ext.grid.Panel', {
			title : 'Customer Records',
			store : custStore,
			columns : [ {
				text : 'Netsuite ID',
				sortable: true,
				xtype : 'templatecolumn',
				dataIndex:'customerID',
				tpl : '{customerID}',
				sortable: true,
                width : 100,
				flex : 0,
				hidden: true
			}, {
				text : 'Customer Name',
				xtype : 'templatecolumn',
				dataIndex:'customerName',
				tpl : '{customerName}',
				sortable: true,
				width : 100,
				//flex : 1
			}, {
				text : 'Status',
				xtype : 'templatecolumn',
				dataIndex:'accountStatus',
				tpl : '{accountStatus}',
				sortable: true,
				width : 60
			}, {
				text : 'Sales Order',
				//xtype : 'templatecolumn',
				dataIndex:'salesOrder',
				//tpl : '{salesOrder}',
				sortable: true,
				width : 75
			}, {
				text : 'Type',
				xtype : 'templatecolumn',
				dataIndex:'type',
				tpl : '{type}',
				sortable: true,
				width : 70
			}, {
				text : 'Ship Date',
				xtype : 'templatecolumn',
				dataIndex:'shipDate',
				tpl : '{shipDate}',
				sortable: true,
				width : 75
			}, {				
				text : 'Item Description',
				xtype : 'templatecolumn',
				dataIndex:'itemDescription',
				tpl : '{itemDescription}',
				sortable: true,
				width : 170
			},{
				text : 'Item Quantity',
				xtype : 'templatecolumn',
				dataIndex:'itemQuantity',
				tpl : '{itemQuantity}',
				sortable: true,
				width : 78
			},{
				text : 'Warranty Type',
				xtype : 'templatecolumn',
				dataIndex:'warrantyType',
				tpl : '{warrantyType}',
				sortable: true,
				width : 100
			}, {				
				text : 'Warranty Start Date',
				xtype : 'templatecolumn',
				dataIndex:'warrantyStartDate',
				tpl : '{warrantyStartDate}',
				sortable: true,
				width : 105
			},{
				text : 'Warranty End Date',
				xtype : 'templatecolumn',
				dataIndex:'warrantyEndDate',
				tpl : '{warrantyEndDate}',
				sortable: true,
			},{
				text : 'Item Notes',
				xtype : 'templatecolumn',
				dataIndex:'itemNotes',
				tpl : '{itemNotes}',
				sortable: true,
				width : 150
			},{
				text : 'Address',
				xtype : 'templatecolumn',
				dataIndex:'address',
				tpl : '{address}',
				sortable: true,
				width : 300
			},{				
				text : 'Internal ID',
				xtype : 'templatecolumn',
				dataIndex:'internalID',
				tpl : '{internalID}',
				sortable: true,
				width : 100,
				hidden: true
			} ],
			flex: 1,
			height : 600,
			width : '100pct',
			dockedItems : [ {
				xtype : 'pagingtoolbar',
				store : custStore, // same store GridPanel is using
				pageSize : itemsPerPage,
				dock : 'bottom',
				displayInfo : true
			} ],
			renderTo : 'data',
			viewConfig : {
				forceFit : true
			}
		});
		
		custStore.loadPage(1);
	});
</script>
</head>
<body>
<div id="data"></div>
<div style="width:100%;text-align:center;font-weight:bold"><a href="/exportCustomerRecords">Export as csv</a></div>
</body>
</html>