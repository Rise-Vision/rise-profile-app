package com.risedisplay.reports.entities;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class CustomerWrapper implements Serializable
{
	private List<Customer> data;
	private Long total;
	
	public CustomerWrapper() {}

	public CustomerWrapper( List<Customer> data, Long total )
	{
		super();
		this.data = data;
		this.total = total;
	}

	public List<Customer> getData()
	{
		return data;
	}

	public void setData( List<Customer> data )
	{
		this.data = data;
	}

	public Long getTotal()
	{
		return total;
	}

	public void setTotal( Long total )
	{
		this.total = total;
	}
	
	
	/*--------------------------------------------
	|             C O N S T A N T S             |
	============================================*/

	/*--------------------------------------------
	|    I N S T A N C E   V A R I A B L E S    |
	============================================*/

	/*--------------------------------------------
	|         C O N S T R U C T O R S           |
	============================================*/

	/*--------------------------------------------
	|   P U B L I C    A P I    M E T H O D S   |
	============================================*/

	/*--------------------------------------------
	|    N O N - P U B L I C    M E T H O D S   |
	============================================*/

	/*--------------------------------------------
	|  A C C E S S O R S / M O D I F I E R S    |
	============================================*/

	/*--------------------------------------------
	|       I N L I N E    C L A S S E S        |
	============================================*/
}
