package client;

import AuctionApp.*;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CORBA.*;
import javax.swing.*;
import java.awt.EventQueue;
import java.awt.event.*;

public class AuctionClient extends JFrame {

	public static Auction auctionImpl;
	public static int uID;
	
	private static StartupInterface startup;
	private static SellerInterface seller;
	private static BidderInterface bidder;
	
	public static void main(String args[]) {
		try {
			
			//Create and initialize ORB
			ORB orb = ORB.init(args, null);
			
			//Get the root naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			
			//Resolve object reference in naming
			String name = "Auction";
			auctionImpl = AuctionHelper.narrow(ncRef.resolve_str(name));
			
			//Get user ID
			uID = auctionImpl.getNextUserID();
			
			spawnStartupInterface();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}
	}
	
	/**
	 * Triggers a server shutdown if a connection has been established
	 */
	public static void triggerShutdown() {
		if (auctionImpl != null) {
			auctionImpl.shutdown();
		}
	}
	
	/**
	 * Spawn the startup interface (seller/bidder interface selection)
	 */
	public static void spawnStartupInterface() {
		//Start client interface dialogue
		EventQueue.invokeLater(() -> {
			startup = new StartupInterface();
			startup.setVisible(true);
		});
	}
	
	/**
	 * Spawn the seller interface
	 */
	public static void spawnSellerInterface() {
		//Start seller dialogue
		EventQueue.invokeLater(() -> {
			startup.setVisible(false);
			seller = new SellerInterface();
			seller.setVisible(true);
		});
	}
	
	/**
	 * Spawn the bidder interface
	 */
	public static void spawnBidderInterface() {
		//Start bidder dialogue
		EventQueue.invokeLater(() -> {
			startup.setVisible(false);
			bidder = new BidderInterface();
			bidder.setVisible(true);
		});
	}
	
}
