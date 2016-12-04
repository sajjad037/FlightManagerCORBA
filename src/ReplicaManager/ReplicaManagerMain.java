package ReplicaManager;

import java.util.Timer;
import java.util.logging.Logger;

import com.concordia.dist.asg1.Models.Enums;
import com.concordia.dist.asg1.StaticContent.StaticContent;
import com.concordia.dist.asg1.Utilities.CLogger;

import Replica.ReplicaListner;
import Replica.ReplicaMain;

/*
 * Reason of this class:
 * 1) Receive UDP requests from Front End (Result is incorrect, replica is down).
 * 2) If replica is down, restart the respective replica and re-initiate the restore process i.e. logs.) (tIMEoUT PERIOD should be kept in mind)
 * 3) If results are incorrect, it increment the error counter and if the counter > 3, restart the replica as mentioned in step 2.
 * 4) Receive heart beat signals from respective replica (after 30 seconds each).
 * 5) Create instance of its replica but communicate through UDP ONLY.
 */

public class ReplicaManagerMain {
	private static CLogger clogger;
	private final static Logger LOGGER = Logger.getLogger(ReplicaManagerMain.class.getName());
	public static boolean isReplicaAlive = true;
	public static ReplicaMain myReplicaInstance = null;

	public static void main(String[] args) {
		String msg = "";
		try {
			// initialize logger
			clogger = new CLogger(LOGGER, "ReplicaManager/ReplicaManager.log");
			msg = " Replica is UP!";
			clogger.log(msg);
			System.out.println(msg);

			// Start UDP Server
			ReplicaListner server = new ReplicaListner(clogger, StaticContent.REPLICA_SAJJAD_lISTENING_PORT,
					Enums.UDPSender.ReplicaSajjad);
			server.start();
			// server.executeTestMessage();
			server.join();

			if (myReplicaInstance == null)
				myReplicaInstance = new ReplicaMain(false);

			RMHeartBeat heatBeat = new RMHeartBeat();
			Timer timer = new Timer();
			timer.schedule(heatBeat, 0, 15000);

		} catch (Exception e) {
			System.out.println("Sequencer Exception: " + e.getMessage());
		}
	}

	public static void restartReplica() {
		if (isReplicaAlive == false) {
			myReplicaInstance = null;
			myReplicaInstance = new ReplicaMain(true);
		}
	}
}
