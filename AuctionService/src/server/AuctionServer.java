package server;

import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

import AuctionApp.*;

/**
 * Servant class
 * Implements Auction.idl interface
 */
class AuctionImpl extends AuctionPOA {
	private ORB orb;
	
	//Relevant IDs
	private int nextUserID = 1;
	private int sellerID;
	private int highestBidderID;
	
	//Auction status
	private boolean isActive = false;
	private float currentItemPrice;
	private String currentItemDescription;
	
	public void setORB(ORB orb_val) {
		orb = orb_val;
	}
	
	private void initAuction(int uID, String description, float initialPrice) {
		sellerID = uID;
		currentItemDescription = description;
		currentItemPrice = initialPrice;
		highestBidderID = -1;
		isActive = true;
	}
	
	/**
	 * If no auction is in progress, offer to sell an item with the given description at the given initial price.
	 * @param uID Seller's user ID
	 * @param description A brief description of the object to be auctioned
	 * @param initialPrice The bid baseline for this object
	 */
	public void offerItem(int uID, String description, float initialPrice, StringHolder statusHolder) {
		if (isActive) {
			statusHolder.value = "An auction is already active. Please try again later.";
			return;
		}
		
		statusHolder.value = "Item successfully offered.";
		initAuction(uID, description, initialPrice);
	}
	
	/**
	 * Return the next available user ID
	 * @return next available user ID
	 */
	public int getNextUserID() {
		return nextUserID++;
	}
	
	/**
	 * If a bid has been accepted, trigger the sale of the current item. Can only be invoked by seller.
	 * @param uID The seller's user ID, for verification purposes
	 */
	public void sell(int uID, StringHolder statusHolder) {
		if (highestBidderID == -1) {
			statusHolder.value = "Sale failed: no bids have been placed on this item.";
			return;
		}
		if (uID != sellerID) {
			statusHolder.value = "Only the original seller can trigger the sale of an item.";
			return;
		}
		statusHolder.value = "Item successfully sold.";
		highestBidderID = -1;
		isActive = false;
	}
	
	/**
	 * Place a bid on the current item, if an item has been offered. Can only be invoked by buyers.
	 * @param uID Bidder's user ID
	 * @param price Bid price
	 */
	public void bid(int uID, float price, StringHolder statusHolder) {
		if (!isActive) {
			statusHolder.value = "No auction is currently active.";
			return;
		}
		
		if (uID == sellerID) {
			statusHolder.value = "You can not bid on your own item.";
			return;
		}
		
		if (price <= currentItemPrice) {
			statusHolder.value = "Please bid a value higher than the current price.";
			return;
		}
		
		statusHolder.value = "Bid successfully placed.";
		currentItemPrice = price;
		highestBidderID = uID;
	}
	
	
	/**
	 * View the user ID of the current highest bidder. Can only be invoked by seller.
	 * @return User ID of the current highest bidder. -1 if no bids have been placed or no auction is active.
	 */
	public int viewHighBidder() {
		return highestBidderID;
	}

	/**
	 * Determine if the auction is currently active
	 * @return true if active, false otherwise
	 */
	public boolean isActive() {
		return isActive;
	}
	
	/**
	 * Determine if the given user is currently the highest bidder. Will fail if no auction is active.
	 * @param uID The bidder's user ID
	 * @return True if the given user is the highest bidder, false if not
	 */
	public boolean viewBidStatus(int uID, StringHolder statusHolder) {
		if (!isActive) {
			statusHolder.value = "No auction is currently active.";
			return false;
		}
		
		if (uID == highestBidderID) {
			statusHolder.value = "You are currently the highest bidder.";
		} else {
			statusHolder.value = "You are not currently the highest bidder.";
		}
		return (uID == highestBidderID);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
	}
	
	/**
	 * Retrieve auction info: whether or not auction is active, description of current item, current price
	 * @return A formatted string of auction info
	 */
	public String viewAuctionStatus() {
		String status = "";
		status += "AUCTION STATUS: \n";
		status += "Active: ";
		if (isActive) status += "Yes\n";
		else {
			status += "No";
			return status;
		}
		status += "Item description: " + currentItemDescription + "\n";
		status += "Current price: $" + String.format("%.2f",  currentItemPrice);
		status += System.currentTimeMillis();
		return status;
	}

	@Override
	public void shutdown() {
		System.out.println("Auction server shutting down.");
	}
}

/**
 * Server
 */
public class AuctionServer {

	public static void main(String args[]) {
		try {
			//Create & initialize ORB
			ORB orb = ORB.init(args, null);
			
			//Get reference to RootPOA and activate POAManager
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
			
			//Create servant and register it with the ORB
			AuctionImpl auctionImpl = new AuctionImpl();
			auctionImpl.setORB(orb);
			
			//Get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(auctionImpl);
			Auction aref = AuctionHelper.narrow(ref);
			
			//Get root naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			
			//Bind object reference in naming
			String name = "Auction";
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, aref);
			
			System.out.println("Auction Server ready.");
			
			//Wait for client invocations
			orb.run();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}
		
		System.out.println("Auction Server exiting.");
	}
	
}
