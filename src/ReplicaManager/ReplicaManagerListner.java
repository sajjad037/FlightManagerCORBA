package ReplicaManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

import com.concordia.dist.asg1.Models.Enums;
import com.concordia.dist.asg1.Models.UDPMessage;
import com.concordia.dist.asg1.StaticContent.StaticContent;
import com.concordia.dist.asg1.Utilities.CLogger;
import com.concordia.dist.asg1.Utilities.Serializer;



public class ReplicaManagerListner implements Runnable {
	private CLogger clogger;
	private DatagramSocket serverSocket;
	private Thread t = null;
	private boolean continueUDP = true;
	int port = 0;
	Enums.UDPSender machineName;
	private int softwareFailureCount = 0;

	public ReplicaManagerListner(CLogger clogger, int port, Enums.UDPSender machineName) {
		this.clogger = clogger;
		this.port = port;
		this.machineName = machineName;
	}

	@Override
	public void run() {
		try {
			serverSocket = new DatagramSocket(port);
			byte[] receiveData = new byte[StaticContent.UDP_REQUEST_BUFFER_SIZE];
			// byte[] sendData = new byte[SIZE_BUFFER_REQUEST];
			String msg = machineName.toString() + " UDP Server Is UP!";

			System.out.println(msg);
			clogger.log(msg);
			while (continueUDP) {

				// Read request
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);

				byte[] message = Arrays.copyOf(receivePacket.getData(), receivePacket.getLength());
				UDPMessage udpMessage = Serializer.deserialize(message);
				UDPMessage ackMessage = null;
				// Clear received buffer
				receiveData = new byte[StaticContent.UDP_REQUEST_BUFFER_SIZE];
				boolean receivedStatus = false;
				switch (udpMessage.getSender()) {
				case FrontEnd:

					if (true) {
						// ifSoftware Failure
						softwareFailureCount++;
						if (softwareFailureCount >= 3) {
							// Restart the server.
							softwareFailureCount = 0;
							ReplicaManager.ReplicaManagerMain.restartReplica();
						}
					} else if(false)
					{
						//Hardware failure occured.
						softwareFailureCount = 0;
						ReplicaManager.ReplicaManagerMain.restartReplica();
						
					}

					break;

				case RMUlan:
				case RMSajjad:
				case RMUmer:
				case RMFeras:

					switch (udpMessage.getOpernation()) {
					case heatBeat:

						break;

					}

					break;

				default:
					msg = "Unknow Sender : " + udpMessage.getSender();
					System.out.println(msg);
					clogger.log(msg);
					break;
				}

				if (receivedStatus) {
					// Send Acknowledge.
					ackMessage.setStatus(true);
					byte[] sendData = Serializer.serialize(ackMessage);
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
							receivePacket.getAddress(), receivePacket.getPort());
					serverSocket.send(sendPacket);

					// Clear Send buffer
					sendData = new byte[StaticContent.UDP_REQUEST_BUFFER_SIZE];
				}
			}
		} catch (Exception ex) {
			clogger.logException("on starting UDP Server", ex);
			ex.printStackTrace();
		}

	}

	/**
	 * Start the server thread
	 */
	public void start() {
		t = new Thread(this);
		t.start();
	}

	/**
	 * Execute a join on the thread
	 * 
	 * @throws InterruptedException
	 */
	public void join() throws InterruptedException {
		if (t == null)
			return;

		t.join();
	}

	/**
	 * Stop the server thread
	 */
	public void stop() {
		continueUDP = false;
	}

}