// Agent bus in project transport_system_with_auctions

/* Initial beliefs and rules */

willingness(true).
number_of_passengers(0).
application_status("not submitted").

/* Initial goals */

!start_waiting.

/* Plans */

+!start_waiting <- .wait(200);
					.print("is waiting for the start of an applications collection").

+last_notice("The collection of applications has been started."): 
							.my_name(N) & willingness(W) & W == true & number_of_passengers(NP) & capacity(C) & NP < C
																					<- .wait(math.random(500));
																						.send(auctioneer,achieve,add_bus(N));
																						.print("has submitted an application").

+application_status("accepted") <- .print("participates in the auction").

+current_round(CR): point(P) & weight_a(WA) & weight_b(WB) & weight_g(WG) & CR == 1
					 & .my_name(N) & current_auction(CA) & application_status(AS)
					 & AS = "accepted" <- 
					 				.wait(math.random(1000));
					 				makeBids(P,WA,WB,WG,N,CA,BB);
					 				+bus_bids(BB);
					 				!reply.
					 				
+current_round(CR): point(P) & weight_a(WA) & weight_b(WB) & weight_g(WG) & CR > 1
					 & .my_name(N) & current_auction(CA) & application_status(AS)
					 & AS = "accepted" & bus_bids(BB) <- 
					 				.wait(math.random(1000));
					 				findBestBidsCombination(BB);
					 				!reply.

+!reply <- .send(auctioneer,achieve,receive_reply);
			.print("has made bids").

+bus_bids(BB) <- fixateNewBids(BB).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
