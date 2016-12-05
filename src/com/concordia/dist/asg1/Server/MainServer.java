package com.concordia.dist.asg1.Server;

import java.util.logging.Logger;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import com.concordia.dist.asg1.StaticContent.StaticContent;
import com.concordia.dist.asg1.Utilities.CLogger;

import Corba.FlightOperations;
import Corba.FlightOperationsHelper;
import Models.ServerConfig;

/**
 * @author SajjadAshrafCan
 *
 */
public class MainServer {
	private final static Logger LOGGER = Logger.getLogger(MainServer.class.getName());
	private static CLogger clogger;

	/**
	 * This Class Start all Servers.
	 */
	public MainServer() {

	}

	/**
	 * @param args
	 */
	
		
	public static void main(String[] args) {
		try {
			// initialize logger
			clogger = new CLogger(LOGGER, "Server/server.log");

			clogger.log("Server strat Initiated.");

			// Read Configuration File
			clogger.log("Reading Server Configurations.");
			int size = StaticContent.getServersList().serverConfigList.size();

			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// String Server through loops
			for (int i = 0; i < size; i++) {
				ServerConfig serverConfig = StaticContent.getServersList().serverConfigList.get(i);
				startSerevr(orb, args, serverConfig.udpPort, serverConfig.serverName);
			}

			clogger.log("All " + size + " Servers are started Successfully.");
			for (;;) {
				orb.run();
			}

		} catch (Exception ex) {
			clogger.logException("On Server Start.", ex);
			ex.printStackTrace();
		}

	}
	
	public void StartMain(String[] args) {
		try {
			// initialize logger
			clogger = new CLogger(LOGGER, "Server/server.log");

			clogger.log("Server strat Initiated.");

			// Read Configuration File
			clogger.log("Reading Server Configurations.");
			int size = StaticContent.getServersList().serverConfigList.size();

			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// String Server through loops
			for (int i = 0; i < size; i++) {
				ServerConfig serverConfig = StaticContent.getServersList().serverConfigList.get(i);
				startSerevr(orb, args, serverConfig.udpPort, serverConfig.serverName);
			}

			clogger.log("All " + size + " Servers are started Successfully.");
			for (;;) {
				orb.run();
			}

		} catch (Exception ex) {
			clogger.logException("On Server Start.", ex);
			ex.printStackTrace();
		}

	}

	/**
	 * Binding remote Object to naming service.
	 * 
	 * @param orb
	 * @param args
	 * @param udpPortNumber
	 * @param serverName
	 * @throws InvalidName
	 * @throws AdapterInactive
	 * @throws ServantNotActive
	 * @throws WrongPolicy
	 * @throws org.omg.CosNaming.NamingContextPackage.InvalidName
	 * @throws NotFound
	 * @throws CannotProceed
	 */
	public static void startSerevr(ORB orb, String[] args, int udpPortNumber, String serverName)
			throws InvalidName, AdapterInactive, ServantNotActive, WrongPolicy,
			org.omg.CosNaming.NamingContextPackage.InvalidName, NotFound, CannotProceed {

		// get reference to rootpoa &amp;
//		POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
//		// activate the POAManager
//		rootpoa.the_POAManager().activate();
//
//		// create servant and register it with the ORB
//		FlightOperationsImplementation flgOpImp = new FlightOperationsImplementation(udpPortNumber, serverName);
//		flgOpImp.mainFunc();
//		flgOpImp.setORB(orb);
//
//		// get object reference from the servant
//		org.omg.CORBA.Object ref = rootpoa.servant_to_reference(flgOpImp);
//		FlightOperations href = FlightOperationsHelper.narrow(ref);
//
//		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
//		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
//
//		NameComponent path[] = ncRef.to_name(serverName);
//		ncRef.rebind(path, href);
	}
}
