/* Generated by AN DISI Unibo */ 
package it.unibo.walker

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Walker ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				var ARG = 0
				var X_tmp		= "0"
				var Y_tmp		= "0"
		
				var X_home		= "0"
				var Y_home		= "0"
				
				var X_barman		= "5"
				var Y_barman		= "0"
		
				var X_entrancedoor  = "0"
				var Y_entrancedoor  = "4"
			
				var X_exitdoor      = "5"
				var Y_exitdoor      = "4"
		 
				var X_teatable1     = "2"
				var Y_teatable1     = "2"
		
				var X_teatable2     = "4"
				var Y_teatable2     = "2"	
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("---walker | s0")
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("---walker | wait")
					}
					 transition(edgeName="t045",targetState="moveToEntrance",cond=whenRequest("moveToEntrance"))
					transition(edgeName="t046",targetState="moveToTable",cond=whenRequest("moveToTable"))
					transition(edgeName="t047",targetState="moveToBarman",cond=whenRequest("moveToBarman"))
					transition(edgeName="t048",targetState="moveToExit",cond=whenRequest("moveToExit"))
					transition(edgeName="t049",targetState="moveToHome",cond=whenRequest("moveToHome"))
				}	 
				state("moveToEntrance") { //this:State
					action { //it:State
						println("---walker | moveToEntrace")
						request("movetoCell", "movetoCell($X_entrancedoor,$Y_entrancedoor)" ,"waiterwalker" )  
					}
					 transition(edgeName="t050",targetState="respondEntrance",cond=whenReply("atcell"))
				}	 
				state("respondEntrance") { //this:State
					action { //it:State
						println("---walker | respondEntrance")
						answer("moveToEntrance", "moveOk", "OK"   )  
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("moveToTable") { //this:State
					action { //it:State
						println("---walker | moveToTable")
						if( checkMsgContent( Term.createTerm("moveToTable(TABLE)"), Term.createTerm("moveToTable(TABLE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												ARG = payloadArg(0).toString().toInt()
												
												if(ARG == 1){
													X_tmp = X_teatable1
													Y_tmp = Y_teatable1
												}else{
													X_tmp = X_teatable2
													Y_tmp = Y_teatable2
												}
												
						}
						request("movetoCell", "movetoCell($X_tmp,$Y_tmp)" ,"waiterwalker" )  
					}
					 transition(edgeName="t051",targetState="respondTeaTable",cond=whenReply("atcell"))
				}	 
				state("respondTeaTable") { //this:State
					action { //it:State
						println("---walker | respondTeaTable ")
						answer("moveToTable", "moveOk", "OK"   )  
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("moveToBarman") { //this:State
					action { //it:State
						println("---walker | moveToBarman")
						request("movetoCell", "movetoCell($X_barman,$Y_barman)" ,"waiterwalker" )  
					}
					 transition(edgeName="t052",targetState="respondBarman",cond=whenReply("atcell"))
				}	 
				state("respondBarman") { //this:State
					action { //it:State
						println("---walker | respondBarman")
						answer("moveToBarman", "moveOk", "OK"   )  
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("moveToExit") { //this:State
					action { //it:State
						println("---walker | moveToExit")
						request("movetoCell", "movetoCell($X_exitdoor,$Y_exitdoor)" ,"waiterwalker" )  
					}
					 transition(edgeName="t053",targetState="respondExit",cond=whenReply("atcell"))
				}	 
				state("respondExit") { //this:State
					action { //it:State
						println("---walker | respondExit")
						answer("moveToExit", "moveOk", "OK"   )  
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("moveToHome") { //this:State
					action { //it:State
						println("---walker | moveToHome")
						request("movetoCell", "movetoCell($X_home,$Y_home)" ,"waiterwalker" )  
					}
					 transition(edgeName="t054",targetState="respondHome",cond=whenReply("atcell"))
				}	 
				state("respondHome") { //this:State
					action { //it:State
						println("---walker | respondHome")
						answer("moveToHome", "moveOk", "OK"   )  
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}
