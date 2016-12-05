package Replica;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;

import com.concordia.dist.asg1.StaticContent.StaticContent;
import com.concordia.dist.asg1.Utilities.Serializer;

import Models.Enums;
import Models.UDPMessage;



public class TestSequencerToReplica {

	public static void main(String[] args) {
		UDPMessage udpMsg = new UDPMessage(Enums.UDPSender.FrontEnd, 1, Enums.getFlightCitiesFromString("Montreal"),
				Enums.Operations.bookFlight, Enums.UDPMessageType.Request);
		HashMap<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("firstName", "Ulan");
		parameterMap.put("lastName", "Baitassov");
		parameterMap.put("address", "Verdun");
		parameterMap.put("phone", "5145606164");
		parameterMap.put("destination", "Washington");
		parameterMap.put("date", "02/12/2016");
		parameterMap.put("classFlight", "economy");
		udpMsg.setParamters(parameterMap);		
		udpMsg.setManagerID("-1");		
		udpMsg.setFrontEndPort(1);	
		
		boolean status = UPDCall(StaticContent.REPLICA_SAJJAD_IP_ADDRESS,
				StaticContent.REPLICA_SAJJAD_lISTENING_PORT, StaticContent.SEQUENCER_ACK_PORT_FOR_REPLICA_SAJJAD,
				udpMsg, Enums.UDPSender.ReplicaSajjad);
		
//		boolean status = UPDCall(StaticContent.REPLICA_UMER_IP_ADDRESS,
//				StaticContent.REPLICA_UMER_lISTENING_PORT, StaticContent.SEQUENCER_ACK_PORT_FOR_REPLICA_UMER,
//				udpMsg, Enums.UDPSender.ReplicaUmer);
	}
	
	private static boolean UPDCall(String destinationIP, int destinationPort, int acknowledgementPort,
			UDPMessage udpMessage, Enums.UDPSender sendsTo) {
		boolean reply = false;
		String msg = "";
		msg = Enums.UDPSender.Sequencer.toString() + " Sending to " + sendsTo.toString() + " " + destinationIP + ":"
				+ destinationPort + ", with listening Acknowledment on : " + acknowledgementPort;
		System.out.println(msg);
		
		DatagramSocket socket = null;
		DatagramSocket clientSocket = null;

		try {

//			UDPMessage udpMessage = null;
//			Map map = Collections.synchronizedMap(udpMessageMap);
//			Set set = map.entrySet();
			// Get an iterator
//			Iterator i = set.iterator();
			clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(destinationIP);

			// Send All Messages from Buffer one by one
//			while (i.hasNext()) {
//				Map.Entry me = (Map.Entry) i.next();
//				udpMessage = (UDPMessage) me.getValue();

				// Serialize udpMessage
				byte[] sendData = Serializer.serialize(udpMessage);
				// Compute ChecK sum here

				// Send UDP Message
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, destinationPort);
				clientSocket.send(sendPacket);

				// socket= new DatagramSocket();
				try {
					// Set Time Out, and Wait For Ack
					clientSocket.setSoTimeout(StaticContent.UDP_RECEIVE_TIMEOUT);
					byte[] receiveData = new byte[StaticContent.UDP_REQUEST_BUFFER_SIZE];
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					clientSocket.receive(receivePacket);
					byte[] message = Arrays.copyOf(receivePacket.getData(), receivePacket.getLength());
					//
					// Deserialize Data to udpMessage Object.
					UDPMessage udpMessageReceived = Serializer.deserialize(message);
					receiveData = new byte[StaticContent.UDP_REQUEST_BUFFER_SIZE];
					switch (udpMessageReceived.getSender()) {
					case ReplicaUlan:
					case ReplicaSajjad:
					case ReplicaUmer:
					case ReplicaFeras:
						msg = "Reply FROM " + udpMessageReceived.getSender().toString() + " SERVER:"
								+ udpMessageReceived.getSequencerNumber();
						System.out.println(msg);
						//clogger.log(msg);
						if (udpMessageReceived.getSequencerNumber() == udpMessage.getSequencerNumber()) {
							//clearBuffer(udpMessageReceived);
							System.out.println("Successfully Reveived!");
							reply = true;
						}

						break;

					default:
						reply = false;
					}

				} catch (SocketTimeoutException e) {
					System.out.println("Time Out occor!");
					reply = false;
					// resend
					// socket.send(data);
					// continue;
				}

			//}

		} catch (Exception ex) {
			// reply = "Error: encouter on " + ServerName + ", Message: " +
			// ex.getMessage();
			//clogger.logException("on starting UDP Server", ex);
			ex.printStackTrace();
			reply = false;
		}

		finally {
			if (socket != null && !socket.isClosed())
				socket.close();
		}

		return reply;
	}
}
