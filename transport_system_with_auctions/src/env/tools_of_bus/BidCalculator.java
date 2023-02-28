// CArtAgO artifact code for project transport_system_with_auctions

package tools_of_bus;

import java.util.ArrayList;
import java.util.List;

import cartago.*;
import models.Auction;
import models.Bid;
import models.ComplexBid;
import models.Passenger;
import tools_of_auctioneer.NoticeBoard;

public class BidCalculator extends Artifact {
	
	public static int numOfBids = 0;
	public static List<ComplexBid> complexBidSecondRound = new ArrayList<>();
	public static List<ComplexBid> complexBidThirdRound = new ArrayList<>();

	@OPERATION
	public void makeBids(String busPoint, double weightA, double weightB, double weightG,
			String busName, Auction auction, OpFeedbackParam <List<Bid>> newBids) {
		
		double[] weights = {weightA/100, weightB/100, weightG/100};
		double[] bids = calculateBids(busPoint, weights, auction.getPassengersList());
		List<Bid> bidsList = new ArrayList<>();
		
		for(int i = 0; i < bids.length; i++) {
			Bid bid = new Bid(++numOfBids,
					auction.getPassengersList().get(i).getName(), busName, bids[i]);
			bidsList.add(bid);
		}
		newBids.set(bidsList);
	}
	
	public double[] calculateBids(String busPoint, double[] weights, List<Passenger> passengerList) {
		double[] bids = new double[passengerList.size()];
		for(int i = 0; i < bids.length; i++) {
			bids[i] += (-1) * weights[0] * passengerList.get(i).getPrice() 
					+ weights[1] * calculatePathsLength(busPoint,passengerList.get(i)) + weights[2] * 360;
			bids[i] = Math.ceil(bids[i] * Math.pow(10, 2)) / Math.pow(10, 2);
		}
		return bids;
	}
	
	public int calculatePathsLength(String busPoint, Passenger passenger) {
		int length = 0;
		length+=PathBuilder.getLengthOfPath(busPoint, passenger.getStartPoint());
		length+=PathBuilder.getLengthOfPath(passenger.getStartPoint(), passenger.getEndPoint()); 
		return length;
	}
	
	@OPERATION
	public void getClass(Object obj) {
		Class class1 = obj.getClass();
		System.out.println("Object's class - " + class1);
	}
	
	@OPERATION
	public void findBestBidsCombination(ArrayList firstBusBids) { 
		
		if(NoticeBoard.currentRound == 2) {
			List<Bid> busBids = new ArrayList<>();
			busBids.addAll(firstBusBids);
			Bid minBid = busBids.get(0);
			for(int i=1; i < busBids.size(); i++) {
				if (minBid.getBusBid() > busBids.get(i).getBusBid()) {
					minBid = busBids.get(i);
				}
			}
			Bid nextMinBid = (minBid.getId() == busBids.get(0).getId())? busBids.get(1) : busBids.get(0);
			for(int i=0; i < busBids.size(); i++) {
				if (nextMinBid.getBusBid() > busBids.get(i).getBusBid() && minBid.getId() != busBids.get(i).getId()) {
					nextMinBid = busBids.get(i);
				}
			}
			ComplexBid complexBid = new ComplexBid(minBid,nextMinBid);
			complexBidSecondRound.add(complexBid);
			System.out.println(minBid + " and " + nextMinBid);
		}
		if(NoticeBoard.currentRound == 3) {
			List<Bid> busBids = new ArrayList<>();
			busBids.addAll(firstBusBids);
			Bid minBid = busBids.get(0);
			for(int i=1; i < busBids.size(); i++) {
				if (minBid.getBusBid() > busBids.get(i).getBusBid()) {
					minBid = busBids.get(i);
				}
			}
			Bid nextMinBid = (minBid.getId() == busBids.get(0).getId())? busBids.get(1) : busBids.get(0);
			for(int i=0; i < busBids.size(); i++) {
				if (nextMinBid.getBusBid() > busBids.get(i).getBusBid() && minBid.getId() != busBids.get(i).getId()) {
					nextMinBid = busBids.get(i);
				}
			}
			Bid nextMinBid2 = (minBid.getId() == busBids.get(0).getId())? ((nextMinBid.getId() == busBids.get(1).getId())? busBids.get(2) : busBids.get(1)) : busBids.get(0);
			for(int i=0; i < busBids.size(); i++) {
				if (nextMinBid2.getBusBid() > busBids.get(i).getBusBid()
						&& minBid.getId() != busBids.get(i).getId() && nextMinBid.getId() != busBids.get(i).getId()) {
					nextMinBid2 = busBids.get(i);
				}
			}
			ComplexBid complexBid = new ComplexBid(minBid,nextMinBid, nextMinBid2);
			complexBidThirdRound.add(complexBid);
			System.out.println(minBid + " and " + nextMinBid + " and " + nextMinBid2);

		}
	}
	
}

