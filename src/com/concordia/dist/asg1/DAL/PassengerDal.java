package com.concordia.dist.asg1.DAL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.concordia.dist.asg1.Models.Enums;
import com.concordia.dist.asg1.Models.Passenger;
import com.concordia.dist.asg1.Models.Response;
import com.concordia.dist.asg1.Utilities.FileStorage;

/**
 * Data access Layer Handle Passengers data.
 * 
 * @author SajjadAshrafCan
 * 
 */
public class PassengerDal {
	private HashMap<String, ArrayList<Passenger>> passengerData = null;
	private int lastBookingId = 0;

	/**
	 * Constructor for PassengerDal
	 * 
	 * @param passengerData
	 * @param lastBookingId
	 */
	public PassengerDal() {
		if (this.passengerData == null) {
			this.passengerData = new HashMap<String, ArrayList<Passenger>>();
		}
	}

	public PassengerDal(HashMap<String, ArrayList<Passenger>> passengerData) {
		this.passengerData = passengerData;
	}

	/**
	 * Book Flight
	 * 
	 * @param passengerInfo
	 * @return
	 */
	public Response bookFlight(Passenger passengerInfo) {
		ArrayList<Passenger> passengerList = null;
		Response response = new Response();
		String key = passengerInfo.getLastName().charAt(0) + "";
		passengerInfo.setBookingId(++lastBookingId);

		// Check Size
		if (passengerData != null && passengerData.size() > 0) {
			// Find the Key (already Exist or not)
			passengerList = passengerData.get(key);
			if (passengerList != null) {
				// get Old list and new Info
				passengerList = passengerData.get(key);
				addToHashMap(key, passengerList, passengerInfo);
			} else {
				// create new List then add
				passengerList = new ArrayList<Passenger>();
				addToHashMap(key, passengerList, passengerInfo);
			}
		} else {
			passengerList = new ArrayList<Passenger>();
			addToHashMap(key, passengerList, passengerInfo);
		}

		response.returnID = lastBookingId;
		response.status = true;
		response.message = "New recode successfully added.";

		return response;
	}

	/**
	 * Get Booked Flight Count.
	 * 
	 * @param recordType
	 * @return
	 */
	public int getBookedFlightCount(String recordType) {
		int flightCount = 0;
		if (passengerData.size() > 0) {
			boolean isAll = recordType.toLowerCase().equals("all");
			Enums.Class flightClass = Enums.getClassFromString(recordType);
			// Get a set of the entries
			Map map = Collections.synchronizedMap(passengerData);
			Set set = map.entrySet();
			// Set set = passengerData.entrySet();
			// synchronized (map) {
			// Get an iterator
			Iterator i = set.iterator();

			// Display elements
			while (i.hasNext()) {
				Map.Entry me = (Map.Entry) i.next();
				ArrayList<Passenger> passengerList = (ArrayList<Passenger>) me.getValue();
				if (isAll) {
					flightCount = flightCount + passengerList.size();

				} else {
					switch (flightClass) {
					case First:
					case Business:
					case Economy:
						for (int j = 0; j < passengerList.size(); j++) {
							Enums.Class _class = passengerList.get(j).getclass();
							if (_class == flightClass) {
								flightCount = flightCount + 1;
							}
						}

						break;
					default:
						break;
					}
				}
			}
			// }

		}
		return flightCount;
	}

	/**
	 * Delete all booking for a flight
	 * 
	 * @param flightId
	 * @return
	 */
	public Response deleteAllBookingForFlight(int flightId) {
		Response response = new Response();
		int countDelete = 0;
		if (passengerData.size() > 0) {

			Map map = Collections.synchronizedMap(passengerData);
			Set set = map.entrySet();
			// synchronized (map) {
			Iterator i = set.iterator();

			ArrayList<String> keysToRemove = new ArrayList<String>();
			;
			ArrayList<Integer> indexToRemove;

			while (i.hasNext()) {
				indexToRemove = new ArrayList<Integer>();

				Map.Entry me = (Map.Entry) i.next();
				String key = me.getKey().toString();
				ArrayList<Passenger> passengerList = (ArrayList<Passenger>) me.getValue();

				for (int index = 0; index < passengerList.size(); index++) {
					if (passengerList.get(index).getFlightId() == flightId) {
						indexToRemove.add(index);
						++countDelete;
					}
				}

				// Look if we need to remove an remove from passenger
				// information.
				int count = indexToRemove.size();
				if (count > 0) {
					for (int j = 0; j < count; j++) {
						Passenger info = passengerList.get(j);
						boolean statu = passengerList.remove(info);
					}

					if (passengerList.size() > 0) {
						passengerData.put(key, passengerList);
					} else {
						keysToRemove.add(key);
					}
					// reset index remove list
					indexToRemove = new ArrayList<Integer>();
				}
				// System.out.print(me.getKey() + ": ");
				// System.out.println(me.getValue());
			}

			// Look for if we need to remove a key
			if (keysToRemove.size() > 0) {
				for (int idx = 0; idx < keysToRemove.size(); idx++) {
					passengerData.remove(keysToRemove.get(idx));
				}
			}

			if (countDelete > 0) {
				response.status = true;
				response.message = countDelete + " records are deleted successfully from Passengers Data.";
			} else {
				response.status = false;
				response.message = "0 records is deleted for that FlightID:" + flightId;
			}
			// }
		} else {
			response.status = false;
			response.message = "There is no Passcenger data to Delete.";
		}
		return response;
	}

	public Response editFlightRecordChanges(int flightId, Enums.Class flightClass, int seatToDelete) {
		Response response = new Response();
		int countDelete = 0;
		if (passengerData.size() > 0) {

			Map map = Collections.synchronizedMap(passengerData);
			Set set = map.entrySet();
			// synchronized (map) {
			Iterator i = set.iterator();

			ArrayList<String> keysToRemove = new ArrayList<String>();
			;
			ArrayList<Integer> indexToRemove;

			while (i.hasNext()) {
				indexToRemove = new ArrayList<Integer>();

				Map.Entry me = (Map.Entry) i.next();
				String key = me.getKey().toString();
				ArrayList<Passenger> passengerList = (ArrayList<Passenger>) me.getValue();

				for (int index = 0; index < passengerList.size(); index++) {
					if (passengerList.get(index).getFlightId() == flightId
							&& passengerList.get(index).getclass().equals(flightClass)) {						
						if (seatToDelete != 0) {
							--seatToDelete;
							indexToRemove.add(index);
							++countDelete;
						}
					}
				}

				// Look if we need to remove an remove from passenger
				// information.
				int count = indexToRemove.size();
				if (count > 0) {
					for (int j = 0; j < count; j++) {
						Passenger info = passengerList.get(j);
						boolean statu = passengerList.remove(info);
					}

					if (passengerList.size() > 0) {
						passengerData.put(key, passengerList);
					} else {
						keysToRemove.add(key);
					}
					// reset index remove list
					indexToRemove = new ArrayList<Integer>();
				}
				// System.out.print(me.getKey() + ": ");
				// System.out.println(me.getValue());
			}

			// Look for if we need to remove a key
			if (keysToRemove.size() > 0) {
				for (int idx = 0; idx < keysToRemove.size(); idx++) {
					passengerData.remove(keysToRemove.get(idx));
				}
			}

			if (countDelete > 0) {
				response.status = true;
				response.message = countDelete + " records are deleted successfully from Passengers Data.";
			} else {
				response.status = false;
				response.message = "0 records is deleted for that FlightID:" + flightId;
			}
			// }
		} else {
			response.status = false;
			response.message = "There is no Passcenger data to Delete.";
		}
		return response;
	}

	public Response deleteBooking(int bookingID) {
		Response response = new Response();
		int countDelete = 0;
		if (passengerData.size() > 0) {

			Map map = Collections.synchronizedMap(passengerData);
			Set set = map.entrySet();
			// synchronized (map) {
			Iterator i = set.iterator();

			ArrayList<String> keysToRemove = new ArrayList<String>();
			;
			ArrayList<Integer> indexToRemove;

			while (i.hasNext()) {
				indexToRemove = new ArrayList<Integer>();

				Map.Entry me = (Map.Entry) i.next();
				String key = me.getKey().toString();
				ArrayList<Passenger> passengerList = (ArrayList<Passenger>) me.getValue();

				for (int index = 0; index < passengerList.size(); index++) {
					if (passengerList.get(index).getBookingId() == bookingID) {
						indexToRemove.add(index);
						++countDelete;
						break;
					}
				}

				// Look if we need to remove an remove from passenger
				// information.
				int count = indexToRemove.size();
				if (count > 0) {
					for (int j = 0; j < count; j++) {
						Passenger info = passengerList.get(j);
						boolean statu = passengerList.remove(info);
					}

					if (passengerList.size() > 0) {
						passengerData.put(key, passengerList);
					} else {
						keysToRemove.add(key);
					}
					// reset index remove list
					indexToRemove = new ArrayList<Integer>();
					break;
				}
				// System.out.print(me.getKey() + ": ");
				// System.out.println(me.getValue());
			}

			// Look for if we need to remove a key
			if (keysToRemove.size() > 0) {
				for (int idx = 0; idx < keysToRemove.size(); idx++) {
					passengerData.remove(keysToRemove.get(idx));
				}
			}

			if (countDelete > 0) {
				response.status = true;
				response.message = countDelete + " records are deleted successfully from Passengers Data.";
			} else {
				response.status = false;
				response.message = "0 records is deleted for that bookingID:" + bookingID;
			}
			// }
		} else {
			response.status = false;
			response.message = "There is no Passcenger data to Delete.";
		}
		return response;
	}

	/**
	 * If booking Id is valid then response.message contains the booking data
	 * with ':' separated string, response.returnID contain the index in array
	 * list
	 * 
	 * @param bookingId
	 * @return
	 */

	public Response getBookingDetails(int bookingId) {
		Response response = new Response();
		response.message = "Invalid Booking Id";
		response.status = false;
		StringBuilder sbPassengerInfo = new StringBuilder();
		if (passengerData != null && passengerData.size() > 0) {

			Map map = Collections.synchronizedMap(passengerData);
			Set set = map.entrySet();
			// synchronized (map) {
			Iterator i = set.iterator();

			while (i.hasNext()) {
				Map.Entry me = (Map.Entry) i.next();
				String key = me.getKey().toString();
				ArrayList<Passenger> passengerList = (ArrayList<Passenger>) me.getValue();

				for (int index = 0; index < passengerList.size(); index++) {
					if (passengerList.get(index).getBookingId() == bookingId) {
						Passenger passenger = passengerList.get(index);
						sbPassengerInfo.append(String.format("%d:%s:%s:%s:%s:%s:%s:%s", passenger.getFlightId(),
								passenger.getFirstName(), passenger.getLastName(), passenger.getAddress(),
								passenger.getPhone(), passenger.getDestination(), passenger.getDate(),
								passenger.getclass()));
						response.message = sbPassengerInfo.toString();
						response.returnID = index;
						response.status = true;
						break;
					}
				}

				// Break while loop
				if (response.status) {
					break;
				}
			}
			// }
		} else {
			response.message = "No Booking data.";
			response.status = false;
		}
		return response;
	}

	public Passenger getBookingDetailObject(int bookingId) {
		Passenger passenger = null;
		if (passengerData != null && passengerData.size() > 0) {

			Map map = Collections.synchronizedMap(passengerData);
			Set set = map.entrySet();
			// synchronized (map) {
			Iterator i = set.iterator();

			while (i.hasNext()) {
				Map.Entry me = (Map.Entry) i.next();
				String key = me.getKey().toString();
				ArrayList<Passenger> passengerList = (ArrayList<Passenger>) me.getValue();

				for (int index = 0; index < passengerList.size(); index++) {
					if (passengerList.get(index).getBookingId() == bookingId) {
						passenger = passengerList.get(index);
						break;
					}
				}
				// Break while loop
				if (passenger != null) {
					break;
				}
			}
			// }
		}
		return passenger;
	}

	public Response isvalidBooking(int bookingId) {
		Response response = new Response();
		response.status = false;
		response.message = "Invalid Booking ID";

		boolean isfound = false;
		if (passengerData.size() > 0) {

			Map map = Collections.synchronizedMap(passengerData);
			Set set = map.entrySet();
			// synchronized (map) {
			Iterator i = set.iterator();

			while (i.hasNext()) {
				Map.Entry me = (Map.Entry) i.next();
				String key = me.getKey().toString();
				ArrayList<Passenger> passengerList = (ArrayList<Passenger>) me.getValue();

				for (int index = 0; index < passengerList.size(); index++) {
					if (passengerList.get(index).getBookingId() == bookingId) {
						isfound = true;
						response.status = true;
						response.message = "Booking ID is valid.:" + passengerList.get(index).getLastName() + ":"
								+ index;
						response.returnID = index;
						break;
					}
				}

				// Break while loop
				if (isfound) {
					break;
				}
			}
			// }
		} else {
			response.status = false;
			response.message = "No Booking Data";
		}
		return response;
	}

	/**
	 * Get All Booking Details.
	 * 
	 * @return
	 */
	public Response getBookingDetails() {
		Response response = new Response();
		response.status = false;
		response.message = "No Booking details aviable.";
		StringBuilder sb = new StringBuilder();
		if (passengerData != null && passengerData.size() > 0) {
			sb.append(
					"BookingID   FirstName   LastName        Address         Phone            Destination     Date      FlightClass      FlightId\r\n");

			Map map = Collections.synchronizedMap(passengerData);
			Set set = map.entrySet();
			Passenger passenger;

			// synchronized (map) {
			Iterator i = set.iterator();

			while (i.hasNext()) {
				Map.Entry me = (Map.Entry) i.next();
				String key = me.getKey().toString();
				ArrayList<Passenger> passengerList = (ArrayList<Passenger>) me.getValue();

				for (int index = 0; index < passengerList.size(); index++) {
					passenger = passengerList.get(index);
					sb.append(String.format(
							"%d           %s      %s          %s      %s       %s      %s          %s       %d  \r\n",
							passenger.getBookingId(), passenger.getFirstName(), passenger.getLastName(),
							passenger.getAddress(), passenger.getPhone(), passenger.getDestination(),
							passenger.getDate(), passenger.getclass(), passenger.getFlightId()));
				}
			}
			// }

			response.status = true;
			response.message = sb.toString();
		}
		return response;
	}

	// public Response cancelBooking(String key, int index, int bookingId,
	// boolean isCanceled) {
	//
	// Response response = new Response();
	// response.status = false;
	// response.message = "Canceled Failed.";
	// ArrayList<Passenger> passengerList = passengerData.get(key);
	// Passenger passenger = passengerList.get(index);
	// if (passenger.getBookingId() == bookingId) {
	// passenger.setCanceled(isCanceled);
	// passengerList.set(index, passenger);
	// passengerData.put(key, passengerList);
	//
	// response.status = false;
	// response.message = "Booking cancel successfully.";
	// } else {
	// response.status = false;
	// response.message = "Booking ID is not valid.";
	// }
	//
	// return response;
	// }

	/**
	 * 
	 * @param key
	 * @param passengerList
	 * @param passengerInfo
	 */
	private void addToHashMap(String key, ArrayList<Passenger> passengerList, Passenger passengerInfo) {
		// synchronized (passengerData) {
		// passengerList.add(passengerInfo);
		// passengerData.put(key, passengerList);
		// }
		synchronized (passengerList) {
			passengerList.add(passengerInfo);
		}
		passengerData.put(key, passengerList);
	}

	/**
	 * @return the passengerData
	 */
	public HashMap<String, ArrayList<Passenger>> getPassengerData() {
		return passengerData;
	}

	/**
	 * @param passengerData
	 *            the passengerData to set
	 */
	public void setPassengerData(HashMap<String, ArrayList<Passenger>> passengerData) {
		this.passengerData = passengerData;
	}

	/**
	 * @return the lastBookingId
	 */
	public int getLastBookingId() {
		return lastBookingId;
	}

}
