// CArtAgO artifact code for project transport_system_with_auctions

package tools_of_auctioneer;

import java.util.ArrayList;
import java.util.List;

import cartago.*;
import models.Bid;
import models.ComplexBid;

public class BidsCollector extends Artifact {
	
	public static List<Bid> allBidsList = new ArrayList<>();
	public static List<Bid> roundBidsList = new ArrayList<>();
	public static List<ComplexBid> roundWinnersList = new ArrayList<>();
	public static ComplexBid winnerBids = new ComplexBid();
	public static ComplexBid endResult = new ComplexBid();

	@OPERATION
	void fixateNewBids(ArrayList <Bid> newBusBidsList) {
		roundBidsList.addAll(newBusBidsList);
	}
}

