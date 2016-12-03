/**
 * 
 */
package com.concordia.dist.asg1.Service;

import java.util.concurrent.CopyOnWriteArrayList;

import com.concordia.dist.asg1.DAL.FlightDAL;
import com.concordia.dist.asg1.Models.Enums;
import com.concordia.dist.asg1.Models.Flight;
import com.concordia.dist.asg1.Models.Response;
import com.concordia.dist.asg1.StaticContent.StaticContent;
import com.concordia.dist.asg1.Utilities.FileStorage;

/**
 * Service layer for Flights, Perform Necessary Function Before and After saving
 * records
 * 
 * @author SajjadAshrafCan
 *
 */
public class FlightService {

	private FlightDAL flightDAL;
	private String fileName = "";

	/**
	 * FlightService Constructor
	 */
	public FlightService() {
		flightDAL = new FlightDAL();
	}

	public FlightService(String ServerName) {
		this.fileName = ServerName + "Flights.txt";
		if (StaticContent.Save_TO_FILES) {
			flightDAL = new FlightDAL(loadFlights());
		} else {
			flightDAL = new FlightDAL();
		}

	}

	/**
	 * Create Flight
	 * 
	 * @param seatsInFirstClass
	 * @param seatsInBusinessClass
	 * @param seatsInEconomyClass
	 * @param flightDate
	 * @param flightTime
	 * @param _destinaition
	 * @param _source
	 * @return
	 */
	public Response createFlight(int seatsInFirstClass, int seatsInBusinessClass, int seatsInEconomyClass,
			String flightDate, String flightTime, String _destinaition, String _source) {
		Response response = new Response();
		int bookedFirstClassSeats = 0, bookedBusinessClassSeats = 0, bookedEconomyClassSeats = 0;
		Enums.FlightCities destination = Enums.getFlightCitiesFromString(_destinaition);
		Enums.FlightCities source = Enums.getFlightCitiesFromString(_source);

		//check flight is already exist
//		response = isFlightAvailable(destination, flightDate);
//		if(response.status)
//		{
//			return response;
//		}
		
		Flight flightInfo = new Flight(bookedFirstClassSeats, bookedBusinessClassSeats, bookedEconomyClassSeats,
				seatsInFirstClass, seatsInBusinessClass, seatsInEconomyClass, flightDate, flightTime, destination,
				source);

		response = flightDAL.CreateFlight(flightInfo);
		if (response.status) {
			saveFlights();
		}
		return response;
	}

	/**
	 * Delete Flight
	 * 
	 * @param passengerService
	 * @param flightID
	 * @return
	 */
	public Response deleteFlight(PassengerService passengerService, int flightID) {
		Response response = flightDAL.deleteFlight(flightID);
		if (response.status) {
			saveFlights();

			String oldMsg = response.message;

			// remove entries of this flight from Passenger.
			response = passengerService.deleteAllBookingForFlight(flightID);
			response.message = oldMsg + "\r\n" + response.message;
		}
		return response;
	}

	/**
	 * Get Flight Details
	 * 
	 * @return
	 */
	public Response flightDetails() {
		return flightDAL.flightDetails();
	}

	/**
	 * Edit Flight Record
	 * 
	 * @param recordID
	 * @param fieldName
	 * @param newValue
	 * @return
	 */

	public Response editFlightRecord(PassengerService passengerService, int recordID, String fieldName,
			String newValue) {
		Response response = flightDAL.editFlightRecord(recordID, fieldName, newValue);
		if (response.status) {
			saveFlights();

			// passengerService
			if (fieldName.equals("seatsInFirstClass") || fieldName.equals("seatsInBusinessClass")
					|| fieldName.equals("seatsInEconomyClass")) {
				Enums.Class _class = null;
				switch (fieldName) {
				case "seatsInFirstClass":
					_class = Enums.Class.First;
					break;
				case "seatsInBusinessClass":
					_class = Enums.Class.Business;
					break;
				case "seatsInEconomyClass":
					_class = Enums.Class.Economy;
					break;

				default:
					break;
				}
				int bookedCount = passengerService.getBookedFlightCount(_class.toString());
				int newFlightSeats = Integer.parseInt(newValue);
				if (newFlightSeats < bookedCount) {
					int seatToDelete = bookedCount - newFlightSeats;
					passengerService.editFlightRecordChanges(recordID, _class, seatToDelete);
				}

			}
		}
		return response;
	}

	/**
	 * is Flight available
	 * 
	 * @param destination
	 * @param date
	 * @param class1
	 * @return
	 */
	public Response isFlightAvailable(Enums.FlightCities destination, String date, Enums.Class class1) {
		return flightDAL.isFlightAvailable(destination, date, class1);
	}
	
	public Response isFlightAvailable(Enums.FlightCities destination, String date) {
		return flightDAL.isFlightAvailable(destination, date);
	}

	/**
	 * Increment or decrement Flight Seats
	 * 
	 * @param flightID
	 * @param class1
	 * @return
	 */

	public Response updateFlightSeats(int flightID, Enums.Class class1, boolean isDecrement) {
		Response response = flightDAL.updateFlightSeats(flightID, class1, isDecrement);
		if (response.status) {
			saveFlights();
		}
		return response;
	}

	/**
	 * get Booked Flight Count
	 * 
	 * @return
	 */
	public Response getBookedFlightCount() {
		return flightDAL.getBookedFlightCount();
	}

	/**
	 * Use in test cases only
	 * 
	 * @param flightID
	 * @return
	 */
	public Flight getFlight(int flightID) {
		return flightDAL.getFlightsData(flightID);
	}

	private void saveFlights() {
		if (StaticContent.Save_TO_FILES) {
			new FileStorage().SaveFlightData(flightDAL.getFlightsData(), this.fileName);
		}
	}

	private CopyOnWriteArrayList<Flight> loadFlights() {
		return new FileStorage().ReadFlightData(this.fileName);
	}
}
