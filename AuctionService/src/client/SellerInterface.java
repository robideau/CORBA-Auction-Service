package client;

import org.omg.CORBA.*;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class SellerInterface extends JFrame {
	
	private JTextField descriptionField, initialPriceField;
	private JTextArea messageLog;
	
	/**
	 * Constructor
	 */
	public SellerInterface() {
		initUI();
	}
	
	/**
	 * Initialize seller UI
	 */
	private void initUI() {		
		
		//Offer item button
		JButton offerItemButton = new JButton("Offer Item");
		offerItemButton.addActionListener((ActionEvent event) -> {		
			String description = descriptionField.getText();
			String initPriceText = initialPriceField.getText();
			float initialPrice = 0.0f;
			
			//Check initial price
			try {
				initialPrice = Float.valueOf(initPriceText);
			} catch (NumberFormatException ex) {
				messageLog.append("Missing or invalid initial price. Setting to $0.00.\n");
			}
			
			//Offer item
			StringHolder sh = new StringHolder();
			AuctionClient.auctionImpl.offerItem(AuctionClient.uID, description, initialPrice, sh);
			messageLog.append(sh.value + "\n");		
			
			//Clear fields
			descriptionField.setText("Item description...");
			initialPriceField.setText("Initial price (optional)...");
		});
		
	
		//Sell button
		JButton sellButton = new JButton("Sell");
		sellButton.addActionListener((ActionEvent event) -> {
			StringHolder sh = new StringHolder();
			AuctionClient.auctionImpl.sell(AuctionClient.uID, sh);
			messageLog.append(sh.value + "\n");
		});
		
		//View high bidder button
		JButton highBidderButton = new JButton("View High Bidder");
		highBidderButton.addActionListener((ActionEvent event) -> {
			int highBidder = AuctionClient.auctionImpl.viewHighBidder();
			if (highBidder == -1) {
				messageLog.append("No bids have been placed on this item.\n");
			} else {
				messageLog.append("Current high bidder: user " + highBidder + "\n");
			}
		});
		
		//View auction status button
		JButton auctionStatusButton = new JButton("View Auction Status");
		auctionStatusButton.addActionListener((ActionEvent event) -> {
			messageLog.append(AuctionClient.auctionImpl.viewAuctionStatus() + "\n");
		});
		
		
		createLayout(offerItemButton, sellButton, highBidderButton, auctionStatusButton);
		
		//Window
		setTitle("Auction - Seller");
		setSize(500, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * Create seller UI layout
	 * @param arg Buttons, in order: Offer Item, Sell, View High Bidder, View Auction Status
	 */
	private void createLayout(JComponent... arg) {
		Container pane = getContentPane();

		GridBagLayout gbl = new GridBagLayout();
		pane.setLayout(gbl);
		GridBagConstraints gbc = new GridBagConstraints();
		
		//Item description text field
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 0, 15);
		gbc.gridx = 0;
		gbc.gridy = 0;
		descriptionField = new JTextField("Item description...", 20);
		pane.add(descriptionField, gbc);
		
		//Item initial price text field
		gbc.gridx = 0;
		gbc.gridy = 1;
		initialPriceField = new JTextField("Initial price (optional)...", 20);
		pane.add(initialPriceField, gbc);
		
		//Offer item button
		gbc.gridx = 0;
		gbc.gridy = 2;
		pane.add(arg[0], gbc);
		
		//Sell item button
		gbc.gridx = 1;
		gbc.gridy = 0;
		pane.add(arg[1], gbc);
		
		//View high bidder button
		gbc.gridx = 1;
		gbc.gridy = 1;
		pane.add(arg[2], gbc);
		
		//Auction status button
		gbc.gridx = 1;
		gbc.gridy = 2;
		pane.add(arg[3], gbc);
		
		//Message log
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(15, 0, 0, 15);
		messageLog = new JTextArea(8, 18);
		JScrollPane scrollPane = new JScrollPane(messageLog);
		messageLog.setEditable(false);
		pane.add(scrollPane, gbc);
		
		pane.requestFocusInWindow();
	}
	
}
