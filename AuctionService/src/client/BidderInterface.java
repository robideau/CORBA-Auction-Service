package client;

import org.omg.CORBA.*;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class BidderInterface extends JFrame {

	private JTextField bidField;
	private JTextArea messageLog;
	
	public BidderInterface() {
		initUI();
	}
	
	private void initUI() {
		
		//Bid button
		JButton bidButton = new JButton("Place Bid");
		bidButton.addActionListener((ActionEvent event) -> {
			String bidText = bidField.getText();
			float bidValue = 0.0f;
			try {
				bidValue = Float.valueOf(bidText);
			} catch (NumberFormatException ex) {
				messageLog.append("Missing or invalid bid value.\n");
				bidValue = -1.0f;
			}
			
			if (bidValue != -1.0f) {
				StringHolder sh = new StringHolder();
				AuctionClient.auctionImpl.bid(AuctionClient.uID, bidValue, sh);
				messageLog.append(sh.value + "\n");
			}
		});
		
		//View bid status button
		JButton bidStatusButton = new JButton("View Bid Status");
		bidStatusButton.addActionListener((ActionEvent event) -> {
			StringHolder sh = new StringHolder();
			AuctionClient.auctionImpl.viewBidStatus(AuctionClient.uID, sh);
			messageLog.append(sh.value + "\n");
		});
		
		//View auction status button
		JButton auctionStatusButton = new JButton("View Auction Status");
		auctionStatusButton.addActionListener((ActionEvent event) -> {
			messageLog.append(System.currentTimeMillis() + "\n");
			messageLog.append(AuctionClient.auctionImpl.viewAuctionStatus() + "\n");
		});
		
		createLayout(bidButton, bidStatusButton, auctionStatusButton);
		
		//Window
		setTitle("Auction - Bidder");
		setSize(500, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void createLayout(JComponent... arg) {
		Container pane = getContentPane();
		
		GridBagLayout gbl = new GridBagLayout();
		pane.setLayout(gbl);
		GridBagConstraints gbc = new GridBagConstraints();
		
		//Bid text field
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 0, 15);
		gbc.gridx = 0;
		gbc.gridy = 0;
		bidField = new JTextField("Bid price...", 20);
		pane.add(bidField, gbc);
		
		//Bid button
		gbc.gridx = 0;
		gbc.gridy = 1;
		pane.add(arg[0], gbc);
		
		//View bid status button
		gbc.gridx = 1;
		gbc.gridy = 0;
		pane.add(arg[1], gbc);
		
		//Auction status button
		gbc.gridx = 1;
		gbc.gridy = 1;
		pane.add(arg[2], gbc);
		
		//Message log
		gbc.gridx = 0;
		gbc.gridy = 2;
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
