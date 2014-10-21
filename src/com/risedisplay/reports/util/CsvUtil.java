package com.risedisplay.reports.util;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.risedisplay.reports.QueryParam;
import com.risedisplay.reports.queue.QueueTask;

import org.apache.commons.codec.net.URLCodec;

public class CsvUtil {
	
	private static final Logger LOGGER = Logger.getLogger("CsvUtil");
	
	
	static public void Enqueue(String data) {
		
		URLCodec encoder = new URLCodec();
		
		String encodedList = "";

		try {
		
			encodedList = encoder.encode(data);
		
			QueueFactory.getDefaultQueue().add(withUrl("/queue")
					.param(QueryParam.TASK, QueueTask.GET_QUOTELIST)
					.param(QueryParam.SYMBOL_CODES, encodedList)
					.method(Method.GET));	
		} catch (Exception e) {
			encodedList = "";
			//log.severe(String.format("Error requesting quoteList %s:  %s", quoteList, e.toString()));
			LOGGER.severe(e.getMessage());
		}
		
			
	}

	public static void parseCsv(String csvData) {
		
		int hoursWindow = 23;
		
		long previousDate = System.currentTimeMillis() - (1000 * 60 * 60 * hoursWindow);

		// check if file already processed in defined window
		Query q = new Query( "CustomerUpdate" );
		q.setFilter(new FilterPredicate("date", FilterOperator.GREATER_THAN, previousDate));
		
		List<Entity> list = DatastoreServiceFactory.getDatastoreService().prepare( q ).asList(FetchOptions.Builder.withDefaults());
		if(list != null && list.size() > 0){
			LOGGER.warning("File already processed in last "+hoursWindow+" hours");
			return;
		}
		
		String codes[] = csvData.split("\\r?\\n");
		
		Entity backupEntity = new Entity("CustomerUpdate");
		backupEntity.setUnindexedProperty("rowcount", (codes.length - 1));
		backupEntity.setProperty("date", System.currentTimeMillis());
		
		DatastoreServiceFactory.getDatastoreService().put(backupEntity);
		
		
		
		String quoteList = "";
		int quoteListSize = 0;
		
		if (codes != null && codes.length > 0) {
			for (int i = 1; i < codes.length; i++) {
				String code = codes[i];
			
				/*try {
				Enqueue(code);
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, "Unable to process row: " + code);
				}*/
				
				try {
					quoteList += code;
					quoteList += "\n";
					
					if (quoteListSize < 1) {
						quoteListSize++;
					
					} else {						
						Enqueue(quoteList);
						quoteList = "";
						quoteListSize = 0;
					}
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, "Unable to process row: " + code);
				}
			}
			if (!quoteList.isEmpty()) {
				
				LOGGER.info("data rows (remainder): ");
				try {
					Enqueue(quoteList);
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, "Unable to process remainder : " + quoteList);
				}
			}
			
			LOGGER.info("Total rows in csv file : " + (codes.length - 1));
		}
		

	}
	
		public static void Execute(String csvData) {
		
		/*String otherThanQuote = " [^\"] ";
        String quotedString = String.format(" \" %s* \" ", otherThanQuote);
        String regex = String.format("(?x) "+ // enable comments, ignore white spaces
                ",                         "+ // match a comma
                "(?=                       "+ // start positive look ahead
                "  (                       "+ //   start group 1
                "    %s*                   "+ //     match 'otherThanQuote' zero or more times
                "    %s                    "+ //     match 'quotedString'
                "  )*                      "+ //   end group 1 and repeat it zero or more times
                "  %s*                     "+ //   match 'otherThanQuote'
                "  $                       "+ // match the end of the string
                ")                         ", // stop positive look ahead
                otherThanQuote, quotedString, otherThanQuote);*/
        
		try {
			
			//String cvsSplitBy = ",";
			String lines[] = csvData.split("\\r?\\n");
			
			//LOGGER.info("No of rows: " + lines.length)
			if (lines != null && lines.length > 0) {
				List<Entity> list = new ArrayList<>();
				for (int i = 0; i < lines.length; i++) {
					try {
				        String[] customerInfo = lines[i].split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

				        LOGGER.info("Adding row - Customer ID:" + customerInfo[0] + ", InteralID:" + customerInfo[13]);
				        
						//String[] customerInfo = lines[i].split(cvsSplitBy);

						// LOGGER.info("No of columns: " + customerInfo.length);
						// Setting Customer info

				     // use internal id as a key
				        Long internalId = Long.parseLong(customerInfo[13]);
				        Entity e = getCustomerById(internalId);
				        if(e == null)	// if null, create new
				        	e = new Entity("Customer", internalId);
						
						e.setProperty("customerID", customerInfo[0]);
						e.setUnindexedProperty("customerName", parseString(customerInfo[1]));
						e.setUnindexedProperty("accountStatus", customerInfo[2]);
						e.setUnindexedProperty("salesOrder", customerInfo[3]);
						e.setUnindexedProperty("type", customerInfo[4]);
						e.setUnindexedProperty("shipDate", customerInfo[5]);
						e.setUnindexedProperty("itemDescription", parseString(customerInfo[6]));
						e.setUnindexedProperty("itemQuantity", customerInfo[7]);
						e.setUnindexedProperty("warrantyType", customerInfo[8]);
						e.setUnindexedProperty("warrantyStartDate", customerInfo[9]);
						e.setUnindexedProperty("warrantyEndDate", customerInfo[10]);
						e.setUnindexedProperty("itemNotes", parseString(customerInfo[11]));
						e.setUnindexedProperty("address", parseString(customerInfo[12]));

						list.add(e);

					} catch (Exception e) {
						LOGGER.log(Level.SEVERE, e.getMessage());
						continue;
					}
				}

				DatastoreServiceFactory.getDatastoreService().put(list);
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

		LOGGER.info("Done Parcing csv files");
	}
	
	private static String parseString(String str) {
		return str.replaceAll("^\"|\"$", "");
	}
	
	private static Entity getCustomerById(Long id){
		try {
			return DatastoreServiceFactory.getDatastoreService().get(KeyFactory.createKey("Customer", id));
		} catch (Exception e) {
			//LOGGER.info("New record Internal ID = " + id.toString());
			return null;
		}
		
	}
}
