// Agent auctioneer in project transport_system_with_auctions

/* Initial beliefs and rules */

number_of_requests(0).
threshold(3).
number_of_bus_participants(0).
round_replies(0).

/* Initial goals */

!start_waiting.

/* Plans */

+!start_waiting <- .wait(500);
						.print("is waiting for requests");
						createAuction(A);
						+auction(A);
						writeNotice("The collection of requests has been started.").

+!add_passenger(N,P,SP,EP,ST): auction(A) & number_of_requests(NR) & threshold(T) & NR < T
																			<- addPassenger(N,P,SP,EP,ST,A);
																				.print("has added a passenger's request");
																				.send(N,tell,request_status("accepted"));
																				+number_of_requests(NR + 1).
																				
+!add_passenger(N,P,SP,EP,ST): number_of_requests(NR) & threshold(T) & NR >= T <- .send(N,tell,request_status("denied")).

+!add_bus(N): auction(A) & number_of_bus_participants(NBP) <- addBus(N,A);
									.print("has added a bus's application");
									.send(N,tell,application_status("accepted"));
									+number_of_bus_participants(NBP+1).

+number_of_requests(NR): threshold(T) & NR == T <- .print("has achieved the threshold");
													writeNotice("The collection of requests has been finished.");
													!collect_applications.
													
+!collect_applications <- writeNotice("The collection of applications has been started.");
							.wait(2000);
							writeNotice("The collection of applications has been finished.");
							!start_auction.

+!start_auction: auction(A) <- writeNotice("The auction has been started.");
								announceParticipants(A);
								writeNoticeRound;
								.print("is waiting for bids");
								.wait(2000);
								!end_round.
													
+!receive_reply: round_replies(RR) <- +round_replies(RR+1).

+!end_round: auction(A) & round_replies(RR) <- +round_replies(0);
												writeNoticeRoundEnd;
												countRoundResults(A);							
												!launch_next_round.
																					
+!launch_next_round: number_of_bus_participants(NP) & current_round(CR) & CR <= NP <- writeNoticeRound;
																					.print("is waiting for bids");
																					.wait(2000);
																					!end_round.
													
+!launch_next_round: number_of_bus_participants(NP) & round_replies(RR) 
												& current_round(CR) & CR > NP <- declareResults;
																					writeNoticeAuctionEnd.
																					//declareResults.
						

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }

