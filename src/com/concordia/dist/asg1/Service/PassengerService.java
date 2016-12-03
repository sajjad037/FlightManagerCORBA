package com.concordia.dist.asg1.Service;

import java.util.ArrayList;
import java.util.HashMap;

import com.concordia.dist.asg1.DAL.PassengerDal;
import com.concordia.dist.asg1.Models.Enums;
import com.concordia.dist.asg1.Models.Passenger;
import com.concordia.dist.asg1.Models.Response;
import com.concordia.dist.asg1.StaticContent.StaticContent;
import com.concordia.dist.asg1.Utilities.FileStorage;

/**
 * Service layer for Passengers, Perform Necessary Function Before and After
 * saving
 * 
 * @author SajjadAshrafCan
 *
 */
public class PassengerService {

	private PassengerDal passengerDal;
	private String fileName = "";

	/**
	 * Constructor of PassengerService
	 */
	public PassengerService() {
		passengerDal = new PassengerDal();
	}

	public PassengerService(String ServerName) {
		this.fileName = ServerName + "Booking.txt";
		if (StaticContent.Save_TO_FILES) {
			passengerDal = new PassengerDal(loadBooking());
		} else {
			passengerDal = new PassengerDal();
		}
	}

	/**
	 * book Flight
	 * 
	 * @param flightService
	 * @param firstName
	 * @param lastName
	 * @param address
	 * @param phone
	 * @param _destination
	 * @param date
	 * @param class1
	 * @return
	 */
	public Response bookFlight(FlightService flightService, String firstName, String lastName, String address,
			String phone, String _destination, String date, String class1) {
		Response response = new Response();
		int flightId = -1;

		Enums.Class _class = Enums.getClassFromString(class1);
		Enums.FlightCities destination = Enums.getFlightCitiesFromString(_destination);

		response = flightService.isFlightAvailable(destination, date, _class);

		// Check Flight is available
		if (response.status) {
			flightId = response.returnID;
			Passenger passengerInfo = new Passenger(flightId, firstName, lastName, address, phone, destination, date,
					_class);
			response = passengerDal.bookFlight(passengerInfo);

			// if Succeed
			if (response.status) {
				saveBooking();
				String oldMsg = response.message;
				int bookingId = response.returnID;
				// update Flights Seats
				response = flightService.updateFlightSeats(flightId, _class, true);
				response.message = oldMsg + "\r\n" + response.message;
				if (response.status) {
					response.returnID = bookingId;
				}
			}
		}
		return response;
	}

	/**
	 * get Booked Flight Count
	 * 
	 * @param recordType
	 * @return
	 */
	public int getBookedFlightCount(String recordType) {
		return passengerDal.getBookedFlightCount(recordType);
	}

	/**
	 * delete All Booking For Flight
	 * 
	 * @param flightID
	 * @return
	 */
	public Response deleteAllBookingForFlight(int flightID) {
		Response response = passengerDal.deleteAllBookingForFlight(flightID);
		if (response.status) {
			saveBooking();
		}
		return response;
	}

	public Response editFlightRecordChanges(int flightID, Enums.Class flightClass, int newSeats) {
		Response response = passengerDal.editFlightRecordChanges(flightID, flightClass, newSeats);

		if (response.status) {
			saveBooking();
		}

		return response;
	}

	// private Response isvalidBooking(int bookingId) {
	// return passengerDal.isvalidBooking(bookingId);
	// }

	/**
	 * Get Booking Detail for booking ID
	 * 
	 * @param bookingId
	 * @return
	 */
	public Response getBookingDetails(int bookingId) {
		return passengerDal.getBookingDetails(bookingId);
	}

	public Passenger getBookingDetailObject(int bookingId) {
		return passengerDal.getBookingDetailObject(bookingId);
	}

	/**
	 * Delete a Booking
	 * 
	 * @param bookingID
	 * @return
	 */
	public Response deleteBooking(int bookingID) {
		Response response = passengerDal.deleteBooking(bookingID);
		if (response.status) {
			saveBooking();
		}
		return response;
	}

	/**
	 * Get Booking Details.
	 * 
	 * @return
	 */
	public Response getBookingDetails() {
		return passengerDal.getBookingDetails();
	}

	// public Response updateBooking(FlightService flightService, int bookingId,
	// boolean isCanceled) {
	// // Check this Booking is exist or not.
	// Response response = new Response();
	// response.status = false;
	// response.message = "Canceled Failed.";
	//
	// // Check this Booking is exist or not.
	// response = getBookingDetails(bookingId);
	// if (response.status) {
	// String passcengerInfo = response.message;
	// String[] valueArray = passcengerInfo.split(":");
	// int flightId = Integer.parseInt(valueArray[1]);
	// String lastName = valueArray[2];
	// Enums.Class _class = Enums.getClassFromString(valueArray[7]);
	// response = passengerDal.cancelBooking(lastName.charAt(0) + "",
	// response.returnID, bookingId, isCanceled);
	//
	// // update flight count.
	// if (response.status) {
	// response.message = passcengerInfo;
	//
	// // booking Canceled then increment
	// if (isCanceled)
	// response = flightService.updateFlightSeats(flightId, _class, false);
	// // Canceled booking enabled
	// else
	// response = flightService.updateFlightSeats(flightId, _class, true);
	//
	// if (response.status) {
	// response.message = passcengerInfo;
	// }
	// }
	// }
	// return response;
	// }

	private void saveBooking() {
		if (StaticContent.Save_TO_FILES) {
			new FileStorage().SaveBookingData(passengerDal.getPassengerData(), this.fileName);
		}
	}

	private HashMap<String, ArrayList<Passenger>> loadBooking() {
		return new FileStorage().ReadBookingData(this.fileName);
	}
}
