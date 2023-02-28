// CArtAgO artifact code for project transport_system_with_auctions

package tools_of_auctioneer;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import cartago.*;
import models.*;
import tools_of_bus.BidCalculator;

public class AuctionManager extends Artifact {
	
	@OPERATION
	void createAuction(OpFeedbackParam <Auction> newAuction) {
		Auction auction = new Auction();
		newAuction.set(auction);
	}

	@OPERATION
	void addPassenger(String name, double price, String startPoint, String endPoint, String startTime, Auction auction) {
		Passenger passenger = new Passenger(name, price, startPoint, endPoint, startTime);
		auction.getPassengersList().add(passenger);
	}
	
	@OPERATION
	void addBus(String name, Auction auction) {
		auction.getBusesList().add(name);
	}
	
	
	@OPERATION
	void countRoundResults(Auction auction) {
		if(NoticeBoard.currentRound == 1) {
			chooseRoundOneWinners(auction);
			for(int i=0; i < BidsCollector.winnerBids.winBids.size(); i++) {
				System.out.println(BidsCollector.winnerBids.winBids.get(i));
			}
			BidsCollector.roundWinnersList.add(NoticeBoard.currentRound-1,BidsCollector.winnerBids);
		}
		if (NoticeBoard.currentRound == 2) {
			ComplexBid roundTwoWinner = BidCalculator.complexBidSecondRound.get(0);
			for(int i=0; i < BidCalculator.complexBidSecondRound.size(); i++) {
				if(roundTwoWinner.getTotalBid() > BidCalculator.complexBidSecondRound.get(i).getTotalBid()) {
					roundTwoWinner = BidCalculator.complexBidSecondRound.get(i);
				}
			}
			BidsCollector.roundWinnersList.add(NoticeBoard.currentRound-1,roundTwoWinner);
			System.out.println(roundTwoWinner);
		}
		if (NoticeBoard.currentRound == 3) {
			ComplexBid roundThreeWinner = BidCalculator.complexBidThirdRound.get(0);
			for(int i=0; i < BidCalculator.complexBidThirdRound.size(); i++) {
				if(roundThreeWinner.getTotalBid() > BidCalculator.complexBidThirdRound.get(i).getTotalBid()) {
					roundThreeWinner = BidCalculator.complexBidThirdRound.get(i);
				}
			}
			BidsCollector.roundWinnersList.add(NoticeBoard.currentRound-1,roundThreeWinner);
			System.out.println(roundThreeWinner);
		}
	}
	
	void chooseRoundOneWinners(Auction auction) {		
		for(int i=0; i < auction.getPassengersList().size(); i++) {
			BidsCollector.winnerBids.winBids.add(i,BidsCollector.roundBidsList.get(i));
			for(int j=1; j < auction.getBusesList().size(); j++) {
				Bid anotherBid = BidsCollector.roundBidsList.get(i+j*auction.getPassengersList().size());
				if(anotherBid.getBusBid() < BidsCollector.winnerBids.winBids.get(i).getBusBid()) {
					BidsCollector.winnerBids.winBids.set(i,BidsCollector.roundBidsList.get(i+j*auction.getPassengersList().size()));
				}

			}
		}
	}
	
	@OPERATION
	public void declareResults() {
		ComplexBid winCombination = (BidsCollector.roundWinnersList.get(0).getTotalBid() 
				> BidsCollector.roundWinnersList.get(2).getTotalBid()) ? BidsCollector.roundWinnersList.get(2): BidsCollector.roundWinnersList.get(0);
		findSecondRoundWinnerCombination();
		winCombination = (winCombination.getTotalBid() > BidsCollector.roundWinnersList.get(2).getTotalBid()) ? BidsCollector.roundWinnersList.get(1): winCombination;
		BidsCollector.endResult = winCombination;
		JOptionPane.showMessageDialog(null, BidsCollector.endResult,"Winner combination", JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	public void findSecondRoundWinnerCombination() {
		for(int i=0; i< 2;i++) {
			if(BidsCollector.roundWinnersList.get(0).winBids.get(i).getBusName() != BidsCollector.roundWinnersList.get(1).winBids.get(0).getBusName() 
					&& BidsCollector.roundWinnersList.get(0).winBids.get(i).getBusName() != BidsCollector.roundWinnersList.get(1).winBids.get(1).getBusName() 
					&& BidsCollector.roundWinnersList.get(0).winBids.get(i).getPassengerName() != BidsCollector.roundWinnersList.get(1).winBids.get(0).getPassengerName()
					&& BidsCollector.roundWinnersList.get(0).winBids.get(i).getPassengerName() != BidsCollector.roundWinnersList.get(1).winBids.get(1).getPassengerName()) {
				BidsCollector.roundWinnersList.get(1).winBids.add(BidsCollector.roundWinnersList.get(0).winBids.get(i));
			}
		}
	}


}

