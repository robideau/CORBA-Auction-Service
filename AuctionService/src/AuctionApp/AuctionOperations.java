package AuctionApp;


/**
* AuctionApp/AuctionOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Auction.idl
* Monday, February 27, 2017 6:14:12 PM CST
*/

public interface AuctionOperations 
{
  void offerItem (int uID, String description, float initialPrice, org.omg.CORBA.StringHolder status);
  int getNextUserID ();
  void sell (int uID, org.omg.CORBA.StringHolder status);
  void bid (int uID, float bidPrice, org.omg.CORBA.StringHolder status);
  int viewHighBidder ();
  boolean viewBidStatus (int uID, org.omg.CORBA.StringHolder status);
  boolean isActive ();
  String viewAuctionStatus ();
  void shutdown ();
} // interface AuctionOperations