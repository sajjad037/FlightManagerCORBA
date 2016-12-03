package com.concordia.dist.asg1.Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

import org.omg.CORBA.ORB;

import com.concordia.dist.asg1.Models.Enums;
import com.concordia.dist.asg1.Models.Passenger;
import com.concordia.dist.asg1.Models.Response;
import com.concordia.dist.asg1.Models.ServerConfig;
import com.concordia.dist.asg1.Service.FlightService;
import com.concordia.dist.asg1.Service.PassengerService;
import com.concordia.dist.asg1.StaticContent.StaticContent;
import com.concordia.dist.asg1.Utilities.CLogger;

import Corba.FlightOperationsPOA;

/**
 * 
 * @author SajjadAshrafCan
 *
 */
public class FlightOperationsImplementation extends FlightOperationsPOA {

	private int UDPPort;
	private String ServerName;
	private FlightService flightService;
	private PassengerService passengerService;
	private CLogger clogger;
	private Logger LOGGER = Logger.getLogger(MainServer.class.getName());
	private ORB orb;

	/**
	 * Constructor
	 * 
	 * @param UDPPort
	 * @param ServerName
	 */
	public FlightOperationsImplementation(int UDPPort, String ServerName) {
		this.UDPPort = UDPPort;
		this.ServerName = ServerName;

		flightService = new FlightService();
		passengerService = new PassengerService();

		// initialize logger
		clogger = new CLogger(LOGGER, "Server/" + this.ServerName + ".log");

	}

	@Override
	public String bookFlight(String firstName, String lastName, String address, String phone, String destination,
			String date, String flightClass) {
		clogger.log("bookFlight(firstName:" + firstName + ", lastName:" + lastName + ", address:" + address + ", phone:"
				+ phone + ", destination:" + destination + ", date:" + date + ", Class:" + flightClass + ")");

		Response response = passengerService.bookFlight(flightService, firstName, lastName, address, phone, destination,
				date, flightClass);

		clogger.log(response.toString());
		//return response.toString();
		return response.status+StaticContent.VALUES_SEPARATOR+response.returnID;
	}

	@Override
	public String getBookedFlightCount(String recordType) {
		StringBuilder sb = new StringBuilder();
		String[] strArray = recordType.split(":");
		recordType = strArray[0];
		String managerID = strArray[1];
		clogger.log(managerID + " requesting to Compute BookedFlightCount for Class " + recordType);

		sb.append(getLocalFlightCount(recordType));

		int count = StaticContent.getServersList().serverConfigList.size();
		Thread[] threads = new Thread[count];
		FlightCountThread[] flightCountThread = new FlightCountThread[count];

		// Start threads
		for (int i = 0; i < count; i++) {
			ServerConfig serverConfig = StaticContent.getServersList().serverConfigList.get(i);
			if (!ServerName.equals(serverConfig.serverName)) {

				flightCountThread[i] = new FlightCountThread(ServerName, "localhost", serverConfig.udpPort,
						"getBookedFlightCount", recordType);
				threads[i] = new Thread(flightCountThread[i]);
				threads[i].start();

			}
		}

		// get data from threads
		for (int i = 0; i < count; i++) {
			if (threads[i] != null) {

				try {
					threads[i].join();
					sb.append(flightCountThread[i].getCountValue());

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		// int count = StaticContent.getServersList().serverConfigList.size();
		// for (int i = 0; i < count; i++) {
		// ServerConfig serverConfig =
		// StaticContent.getServersList().serverConfigList.get(i);
		// if (!ServerName.equals(serverConfig.serverName)) {
		// sb.append(UPDCall(ServerName, "localhost", serverConfig.udpPort,
		// "getBookedFlightCount", recordType));
		// }
		// }

		// sb.append(UPDGetCount("", "localhost", 12121, recordType));
		// sb.append(UPDGetCount(server, ip, port, recordType));
		clogger.log("Response:" + sb.toString());
		return sb.toString();
	}

	@Override
	/*public String editFlightRecord(String recordID, String fieldName, String newValue) {
		String reply = "";
		String[] strArray = recordID.split(":");
		recordID = strArray[0];
		String managerID = strArray[1];

		clogger.log(
				"editFlightRecord(recordID:" + recordID + ", fieldName:" + fieldName + ", newValue:" + newValue + ").");

		Enums.FlightFileds operation = Enums.getEnumFlightFiledsFromString(fieldName);

		switch (operation) {
		case createFlight:
			String[] arr = newValue.split(":");
			// IstCls , BusCls , EconCls , Date , Time , destination.
			reply = createFlight(managerID, Integer.parseInt(arr[0]), Integer.parseInt(arr[1]),
					Integer.parseInt(arr[2]), arr[3], arr[4], arr[5]);
			break;
		case deleteFlight:
			int flightID = Integer.parseInt(newValue);
			reply = deleteFlight(managerID, flightID);
			break;
		case flightDetail:
			reply = flightDetails();
			break;
		case bookingDetail:
			reply = getBookingDetails();
			break;

		case flightDate:
		case flightTime:
		case destinaition:
		case source:
		case seatsInFirstClass:
		case seatsInBusinessClass:
		case seatsInEconomyClass:
			Response response = flightService.editFlightRecord(Integer.parseInt(recordID), fieldName, newValue);
			reply = response.toString();
			break;

		default:
			reply = "This Operation is not defined.";
			break;
		}

		clogger.log(reply);
		return reply;
	}*/
	public String editFlightRecord(String recordID, String fieldName, String newValue) {
		String reply = "";
		String[] strArray = recordID.split(":");
		recordID = strArray[0];
		String managerID = strArray[1];

		clogger.log(
				"editFlightRecord(recordID:" + recordID + ", fieldName:" + fieldName + ", newValue:" + newValue + ").");

		Enums.FlightFileds operation = Enums.getEnumFlightFiledsFromString(fieldName);

		switch (operation) {
		case createFlight:
			String[] arr = newValue.split(":");
			// IstCls , BusCls , EconCls , Date , Time , destination.
			reply = createFlight(managerID, Integer.parseInt(arr[0]), Integer.parseInt(arr[1]),
					Integer.parseInt(arr[2]), arr[3], arr[4], arr[5]);
			break;
		case deleteFlight:
			int flightID = Integer.parseInt(newValue);
			reply = deleteFlight(managerID, flightID);
			break;
		case flightDetail:
			reply = flightDetails();
			break;
		case bookingDetail:
			reply = getBookingDetails();
			break;

		case flightDate:
		case flightTime:
		case destinaition:
		case source:
		case seatsInFirstClass:
		case seatsInBusinessClass:
		case seatsInEconomyClass:
			Response response = flightService.editFlightRecord(passengerService, Integer.parseInt(recordID), fieldName,
					newValue);
			//reply = response.toString();
			reply = response.status+StaticContent.VALUES_SEPARATOR+response.returnID;
			break;

		default:
			//reply = " This Operation is not defined.";
			reply = false+"";
			break;
		}

		clogger.log(reply);
		return reply;
	}
	@Override
	public String transferReservation(String passengerID, String currentCity, String otherCity) {
		// String flightID = "";
		String destination = "";
		Response response = new Response();
		response.status = false;

		// Check source and destination are same
		if (!currentCity.equals(otherCity)) {
			// Get Booking Data, Cancel Booking.
			//response = passengerService.getBookingDetails(Integer.parseInt(passengerID));
			Passenger passenger = passengerService.getBookingDetailObject(Integer.parseInt(passengerID));
			StringBuilder sb = new StringBuilder();

			// Booked to Other Server via UDP Call.
			if (passenger != null) {
				response.message = String.format("%d:%s:%s:%s:%s:%s:%s:%s", passenger.getFlightId(),
						passenger.getFirstName(), passenger.getLastName(), passenger.getAddress(),
						passenger.getPhone(), passenger.getDestination(), passenger.getDate(),
						passenger.getclass());
				String[] infoArray = response.message.split(":");
				// flightID = infoArray[0];
				destination = infoArray[5];

				// Check old destination and new source are same.
				if (!destination.equals(otherCity)) {
					int count = StaticContent.getServersList().serverConfigList.size();
					for (int i = 0; i < count; i++) {
						ServerConfig serverConfig = StaticContent.getServersList().serverConfigList.get(i);
						if (otherCity.equals(serverConfig.serverName)) {
							synchronized (passenger){ 
							sb.append(UPDCall(ServerName, "localhost", serverConfig.udpPort, "transferReservation",
									response.message));
							}
							break;
						}
					}
					// delete that flights
					if (sb != null && sb.toString().length() > 0 && !sb.toString().contains("false")) {
						String oldmgs = sb.toString();
						response = passengerService.deleteBooking(Integer.parseInt(passengerID));
						if (response.status) {
							response.message = oldmgs + ", " + response.message;
						}
					} else {
						response.returnID = -1;
						response.message = sb.toString();
						response.status = false;
					}
				} else {
					response.returnID = -1;
					response.message = "This booking ID : " + passengerID + ", is already destined to " + destination;
					response.status = false;
				}
			}
		} else {
			response.returnID = -1;
			response.message = "failed due to Source and destination are same ";
			response.status = false;
		}

		//return response.toString();
		return response.status +"";
	}

	// public Response updateBooking(int bookingId, boolean isCanceled) {
	// return passengerService.updateBooking(flightService, bookingId,
	// isCanceled);
	// }

	/**
	 * Get Local Flight Count.
	 * 
	 * @param recordType
	 * @return
	 */
	public String getLocalFlightCount(String recordType) {
		int count = passengerService.getBookedFlightCount(recordType);
		clogger.log("getLocalFlightCount(recordType:" + recordType + ") => " + count + ".");
		//return ServerName + " has " + count + ".";
		return StaticContent.getShortServerName(ServerName) + " " + count + ", ";
	}

	/**
	 * Create Flight
	 * 
	 * @param ManagerID
	 * @param seatsInFirstClass
	 * @param seatsInBusinessClass
	 * @param seatsInEconomyClass
	 * @param flightDate
	 * @param flightTime
	 * @param _destinaition
	 * @return
	 */
	private String createFlight(String ManagerID, int seatsInFirstClass, int seatsInBusinessClass,
			int seatsInEconomyClass, String flightDate, String flightTime, String _destinaition) {
		clogger.log(ManagerID + " requesting createFlight(seatsInFirstClass:" + seatsInFirstClass
				+ ", seatsInBusinessClass:" + seatsInBusinessClass + ", seatsInEconomyClass:" + seatsInEconomyClass
				+ ", flightDate:" + flightDate + ", flightTime:" + flightTime + ", destinaition:" + _destinaition
				+ ").");
		Response response = flightService.createFlight(seatsInFirstClass, seatsInBusinessClass, seatsInEconomyClass,
				flightDate, flightTime, _destinaition, ServerName);
		clogger.log(response.toString());
		//return response.toString();
		return response.status+StaticContent.VALUES_SEPARATOR+response.returnID;
	}

	/**
	 * Delete a Flight
	 * 
	 * @param ManagerID
	 * @param flightID
	 * @return
	 */
	private String deleteFlight(String ManagerID, int flightID) {
		clogger.log(ManagerID + " is requesting deleteFlight(flightID:" + flightID + ")");
		Response response = flightService.deleteFlight(passengerService, flightID);
		clogger.log(response.toString());
		//return response.toString();
		return response.status+"";
	}

	/**
	 * Get Flights Details
	 * 
	 * @return
	 */
	private String flightDetails() {
		Response response = flightService.flightDetails();
		String reply = "";
		if (response.status) {
			reply = response.message;
		} else {
			reply = response.toString();
		}
		return reply;

	}

	/**
	 * Get Booking Details
	 * 
	 * @return
	 */
	private String getBookingDetails() {
		Response response = passengerService.getBookingDetails();
		String reply = "";
		if (response.status) {
			reply = response.message;
		} else {
			reply = response.toString();
		}
		return reply;
	}

	/**
	 * set Orb
	 * 
	 * @param orb_val
	 */
	public void setORB(ORB orb_val) {
		orb = orb_val;
	}

	/**
	 * implement shutdown() method
	 */
	public void shutdown() {
		orb.shutdown(false);
	}

	/**
	 * Start UDP Server
	 */
	public void startUDPServer() {
		new Thread(new UDPResponder()).start();
	}

	/**
	 * UDP Call (Client call) to other server
	 * 
	 * @param remoteServer
	 * @param ip
	 * @param port
	 * @param operation
	 * @param augs
	 * @return
	 */
	public String UPDCall(String remoteServer, String ip, int port, String operation, String augs) {
		String reply = "";
		String msg = "";
		msg = "Requesting " + remoteServer + ", Server for operation for augs " + augs + ".";
		System.out.println(msg);
		clogger.log(msg);
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(ip);
			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];
			String request = ServerName + ":" + operation + ":" + augs;
			sendData = request.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			clientSocket.send(sendPacket);
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			String modifiedSentence = new String(receivePacket.getData());
			clientSocket.close();
			msg = "Reply FROM " + remoteServer + " SERVER:" + modifiedSentence.trim();
			System.out.println(msg);
			clogger.log(msg);
			reply = modifiedSentence.trim();
		} catch (Exception ex) {
			reply = "Error: encouter on " + ServerName + ", Message: " + ex.getMessage();
			clogger.logException("on starting UDP Server", ex);
			ex.printStackTrace();
		}

		return reply;
	}

	/**
	 * Save some dummy Data. for requested Server.
	 */
	private void saveDummyData() {
		try {
			clogger.log("saving some dummy data.");

			int count = StaticContent.getServersList().serverConfigList.size();
			for (int i = 0; i < count; i++) {
				ServerConfig serverConfig = StaticContent.getServersList().serverConfigList.get(i);
				if (!serverConfig.serverName.equals(ServerName)) {

					// Save Flight
					createFlight("system", 5, 10, 20, "2016/11/22", "13;14", serverConfig.serverName);

					// Book Fight
					bookFlight("FDummy", "LDummay", "Saint Marc", "1234567890", serverConfig.serverName, "2016/11/22",
							"Economy");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * add some dummy data and start UDP server.
	 */
	public void mainFunc() {
		try {
			// exportServer(args);
			String msg = ServerName + " RMI Server is Up! and running";
			System.out.println(msg);

			startUDPServer();

			// save some Dummy Data
			saveDummyData();

			// Get Flight Data
			// System.out.println(ServerName+"\r\n "+flightDetails());

		} catch (Exception e) {
			e.printStackTrace();
			clogger.logException("on Binding Server", e);
		}
	}

	/**
	 * UDP Server
	 * 
	 * @author SajjadAshrafCan
	 *
	 */
	public class UDPResponder implements Runnable {

		private DatagramSocket serverSocket;

		public void run() {
			try {
				serverSocket = new DatagramSocket(UDPPort);
				byte[] receiveData = new byte[1024];
				byte[] sendData = new byte[1024];
				String msg = ServerName + " UDP Server Is UP!";

				System.out.println(msg);
				clogger.log(msg);
				while (true) {
					// Read request
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					serverSocket.receive(receivePacket);
					String request = new String(receivePacket.getData());

					// Clear received buffer
					receiveData = new byte[1024];

					String[] requestArray = request.trim().split(":");
					String opreation, flightClass, capitalizedSentence = "";

					String remoteServer = requestArray[0];
					opreation = requestArray[1];

					msg = "Request RECEIVED: " + remoteServer + " is requesting for Operation " + opreation
							+ ", with Parameters : " + request + ".";
					System.out.println(msg);
					clogger.log(msg);
					InetAddress IPAddress = receivePacket.getAddress();
					int port = receivePacket.getPort();

					switch (opreation) {
					case "getBookedFlightCount":
						flightClass = requestArray[2];
						capitalizedSentence = getLocalFlightCount(flightClass);
						break;

					case "transferReservation":
						String firstName, lastName, address, phone, destination, date = "";
						// oldBookingId = requestArray[2];
						firstName = requestArray[3];
						lastName = requestArray[4];
						address = requestArray[5];
						phone = requestArray[6];
						destination = requestArray[7];
						date = requestArray[8];
						flightClass = requestArray[9];
						capitalizedSentence = bookFlight(firstName, lastName, address, phone, destination, date,
								flightClass);

						break;

					default:
						break;
					}

					sendData = capitalizedSentence.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					serverSocket.send(sendPacket);

					// Clear Send buffer
					sendData = new byte[1024];
				}
			} catch (Exception ex) {
				clogger.logException("on starting UDP Server", ex);
				ex.printStackTrace();
			}
		}
	}

	/**
	 * UDP Client request Thread.
	 * 
	 * @author SajjadAshrafCan
	 *
	 */
	public class FlightCountThread implements Runnable {

		StringBuilder _sb = new StringBuilder();
		int _udpPort;
		String currentServerName, destinationIPAddress, operation, parameters;

		public FlightCountThread(String currentServerName, String destinationIPAddress, int _udpPort, String operation,
				String parameters) {
			// store parameter for later user
			this._udpPort = _udpPort;
			this.currentServerName = currentServerName;
			this.destinationIPAddress = destinationIPAddress;
			this.operation = operation;
			this.parameters = parameters;
		}

		public void run() {
			_sb.append(UPDCall(currentServerName, destinationIPAddress, _udpPort, operation, parameters));
		}

		public String getCountValue() {
			return _sb.toString();
		}
	}
}
