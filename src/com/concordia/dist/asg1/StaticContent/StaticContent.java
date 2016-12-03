package com.concordia.dist.asg1.StaticContent;

import com.concordia.dist.asg1.Models.Enums;
import com.concordia.dist.asg1.Models.ServersList;
import com.concordia.dist.asg1.Utilities.FileStorage;

/**
 * save all type of Static string, data etc.
 * 
 * @author SajjadAshrafCan
 *
 */
public class StaticContent {
	public static final String dateFormat = "yyyy/MM/dd";
	public static final String timeFormat = "HH;mm";
	private static ServersList serversConfigurations;
	public static final String BASE_PATH = "C:/FlightManagerWebService/";
	public static final boolean Save_TO_FILES = false;
	
	public static final int UDP_REQUEST_BUFFER_SIZE = 4000;
	public static final int UDP_RECEIVE_TIMEOUT = 3000;

	// Error Messages
	public static final String MSG_NUMBER = "Enter a number[0-9].";
	public static final String MSG_PHONE = "Enter a valid Phone # (10 digit, 5141231238).";
	public static final String MSG_ALPHA_NUMERIC_NO_SPACES = "Enter a valid Aplha Numeric (No spaces).";
	public static final String MSG_ALPHA_NUMERIC_WITH_SPACES = "Enter a valid Aplha Numeric (With spaces).";
	public static final String MSG_CITY_NAME = "Enter a valid City (Montreal, Washington, NewDelhi are case sensitive).";
	public static final String MSG_SHORTCITY = "Enter a valid Short City (MTL, WST, NDL are case sensitive).";
	// public static final String ERROR_MSG_SOURCE ="Enter a valid SOURCE
	// (Montreal, Washington, NewDelhi is case sensitive).";
	public static final String MSG_FLIGHT_CLASS = "Enter a valid Flight Class (First, Business, Economy are case sensitive).";
	public static final String MSG_FLIGHT_FIELDS = "Enter a valid Field Name \r\n (flightDate, flightTime, destinaition, source, seatsInFirstClass, seatsInBusinessClass, seatsInEconomyClass, editFlight, createFlight, deleteFlight, flightDetail are case sensitive).";
	public static final String MSG_RECORD_TYPE = "Enter a valid Flight Class (First, Business, Economy are case sensitive).";
	public static final String MSG_DATE = "Enter a valid Date (yyyy/MM/dd, ex: 2016/10/16).";
	public static final String MSG_TIME = "Enter a valid Time (HH:mm, ex: 13;13).";
	public static final String ERROR_GENERAL_MSG = "Error: Invalid, Please ";
	public static final String ERROR_SEPARATOR_FIELD_MSG = "Error: Invalid, ':' Colon is revserved you cannot use in input values.";
	public static final String ERROR_REQUIRED_MSG = "Error: Invalid, Empty value is not accepted.It is required field.";

	// Reserved inputs
	public static final String VALUES_SEPARATOR = ":";
	public static final String VALUES_SEPARATOR_INPUTS = "~";
	public static final String VALUES_SEPARATOR_COMMA = ",";
	
	//Configurations	
	public static final String FRONT_END_IP_ADDRESS = "127.0.0.1";
	public static final int FRONT_END_lISTENING_PORT = 1010;
	public static final int FRONT_END_ACK_PORT = 1010;
	
	public static final String SEQUENCER_IP_ADDRESS = "127.0.0.1";
	public static final int SEQUENCER_lISTENING_PORT = 5555;
	public static final int SEQUENCER_ACK_PORT = 5556;
	
	public static final String RM1_IP_ADDRESS = "127.0.0.1";
	public static final int RM1_lISTENING_PORT = 1012;
	
	public static final String RM2_IP_ADDRESS = "127.0.0.1";
	public static final int RM2_lISTENING_PORT = 1013;

	public static final String RM3_IP_ADDRESS = "127.0.0.1";
	public static final int RM3_lISTENING_PORT = 1014;

	public static final String RM4_IP_ADDRESS = "127.0.0.1";
	public static final int RM4_lISTENING_PORT = 1015;
	
	
	public static final String REPLICA_ULAN_IP_ADDRESS = "127.0.0.1";
	public static final int REPLICA_ULAN_lISTENING_PORT = 1016;
	
	public static final String REPLICA_SAJJAD_IP_ADDRESS = "127.0.0.1";
	public static final int REPLICA_SAJJAD_lISTENING_PORT = 1017;
	
	public static final String REPLICA_UMER_IP_ADDRESS = "127.0.0.1";
	public static final int REPLICA_UMER_lISTENING_PORT = 1018;
	
	public static final String REPLICA_FERAS_IP_ADDRESS = "127.0.0.1";
	public static final int REPLICA_FERAS_lISTENING_PORT = 1019;
	
	
	
	

	/**
	 * Get Server Configuration list.
	 * 
	 * @return
	 */
	public static ServersList getServersList() {
		if (serversConfigurations == null) {
			serversConfigurations = (new FileStorage()).LoadServerConfigurations();
		}
		return serversConfigurations;
	}
	
	public static String getShortServerName(String serverName){
		String shortserverName= "";
		if(serverName.equals(null))
			return shortserverName;
		
		Enums.FlightCities _serverName = Enums.getFlightCitiesFromString(serverName);
		switch (_serverName) {
		case Montreal:
			shortserverName = "MTL";
			break;
		case Washington:
			shortserverName = "WST";
			break;
		case NewDelhi:
			shortserverName = "NDL";
			break;

		default:
			break;
		}
		
		return shortserverName;
	}
}
