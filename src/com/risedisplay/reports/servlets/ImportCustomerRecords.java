package com.risedisplay.reports.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.gson.Gson;
import com.risedisplay.reports.entities.Customer;
import com.risedisplay.reports.entities.CustomerWrapper;

@SuppressWarnings( "serial" )
public class ImportCustomerRecords extends HttpServlet
{
	private Logger LOGGER = Logger.getLogger( "MailHandlerServlet" );
	
	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public void doGet( HttpServletRequest req, HttpServletResponse resp ) throws IOException
	{
		doPost( req, resp );
	}

	public void doPost( HttpServletRequest req, HttpServletResponse resp ) throws IOException
	{
		String startIndex = req.getParameter( "startIndex" );
		String customerId = req.getParameter( "customerid" );
		
		LOGGER.info("start index == "+startIndex);
		
		if (( startIndex != null && startIndex.length() > 0 )){
			int i = Integer.parseInt( startIndex );
			long totalCount = getRecordsCount();
			
			LOGGER.info("total count == "+totalCount);
			
			// If there are any records
			if (totalCount > 0){
				List<Customer> customers = new ArrayList<>();
				//If url has customerId
				if (customerId != null && customerId.length() > 0){
					Query q = new Query( "Customer" );
					q.setFilter(new FilterPredicate("customerID", FilterOperator.EQUAL, customerId));
					
					for ( Entity e : DatastoreServiceFactory.getDatastoreService().prepare( q ).asIterable() ){
						customers.add( getCustomerFromEntity( e ) );
					}
					totalCount = customers.size();
				} else {
					PreparedQuery pq = datastore.prepare( new Query( "Customer" ).addSort( "customerID", SortDirection.ASCENDING ) );
					QueryResultList<Entity> results = pq.asQueryResultList( FetchOptions.Builder.withLimit( 200 ).offset( i ) );
					for ( Entity e : results ){
						customers.add( getCustomerFromEntity( e ) );
					}
				}
				
				req.getSession().setAttribute("customers", customers);	// will be used to export
				resp.setContentType( "application/json" );
				resp.getWriter().print( new Gson().toJson( new CustomerWrapper( customers, totalCount ) ) );
			}
		}
	}

	private long getRecordsCount(){
		try{
			return datastore.prepare( new Query( "Customer" ) ).countEntities( FetchOptions.Builder.withDefaults() );
		}catch ( Exception e ){
			LOGGER.info( "Failed to get total Record count: " + e.getStackTrace());
		}
		return 0;
	}

	private Customer getCustomerFromEntity( Entity e ){
		Customer customer = new Customer();
		customer.setCustomerID( (String) e.getProperty( "customerID" ) == null ? "" : (String) e.getProperty( "customerID" ) );
		customer.setCustomerName( (String) e.getProperty( "customerName" ) == null ? "" : (String) e.getProperty( "customerName" ) );
		customer.setAccountStatus( (String) e.getProperty( "accountStatus" ) == null ? "" : (String) e.getProperty( "accountStatus" ) );
		customer.setSalesOrder( (String) e.getProperty( "salesOrder" ) == null ? "" : (String) e.getProperty( "salesOrder" ) );
		customer.setType( (String) e.getProperty( "type" ) == null ? "" : (String) e.getProperty( "type" ) );
		customer.setShipDate( (String) e.getProperty( "shipDate" ) == null ? "" : (String) e.getProperty( "shipDate" ) );
		customer.setItemDescription( (String) e.getProperty( "itemDescription" ) == null ? "" : (String) e.getProperty( "itemDescription" ) );
		customer.setItemQuantity( (String) e.getProperty( "itemQuantity" ) == null ? "" : (String) e.getProperty( "itemQuantity" ) );
		customer.setWarrantyType( (String) e.getProperty( "warrantyType" ) == null ? "" : (String) e.getProperty( "warrantyType" ) );
		customer.setWarrantyStartDate( (String) e.getProperty( "warrantyStartDate" ) == null ? "" : (String) e.getProperty( "warrantyStartDate" ) );
		customer.setWarrantyEndDate( (String) e.getProperty( "warrantyEndDate" ) == null ? "" : (String) e.getProperty( "warrantyEndDate" ) );
		customer.setItemNotes( (String) e.getProperty( "itemNotes" ) == null ? "" : (String) e.getProperty( "itemNotes" ) );
		customer.setAddress( (String) e.getProperty( "address" ) == null ? "" : (String) e.getProperty( "address" ) );
		customer.setInternalID( String.valueOf( e.getKey().getId() ) == null ? "" : String.valueOf( e.getKey().getId() ) );
		return customer;
	}
}
