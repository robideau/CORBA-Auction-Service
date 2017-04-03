package client;

import AuctionApp.*;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;

public class StartupInterface extends JFrame {

	/**
	 * Constructor
	 */
	public StartupInterface() {
		initUI();
	}
	
	/**
	 * Initialize startup UI
	 */
	private void initUI() {
		
		//Exit button
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener((ActionEvent event) -> {
			System.exit(0);
		});
		
		//Seller interface selection button
		JButton sellerButton = new JButton("Seller Interface");
		sellerButton.addActionListener((ActionEvent event) -> {
			AuctionClient.spawnSellerInterface();
		});
		
		//Bidder interface selection button
		JButton bidderButton = new JButton("Bidder Interface");
		bidderButton.addActionListener((ActionEvent event) -> {
			AuctionClient.spawnBidderInterface();
		});
		
		createButtonLayout(sellerButton, bidderButton, exitButton);
		
		//Window
		setTitle("Auction Service");
		setSize(300, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * Create layout for startup UI
	 * @param arg Buttons
	 */
	private void createButtonLayout(JComponent... arg) {
		Container pane = getContentPane();
		GridLayout gl = new GridLayout(3, 1);
		pane.setLayout(gl);
		for (int i = 0; i < arg.length; i++) {
			pane.add(arg[i]);
		}
		
		pane.requestFocusInWindow();
	}
}
