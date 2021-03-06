/* Generated by AN DISI Unibo */ 
package it.unibo.smartbell

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Smartbell ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				var ID = 0
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("smartBell | s0")
						solve("consult('temperature.pl')","") //set resVar	
						discardMessages = false
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						updateResourceRep( "wait"  
						)
						println("smartBell | wait")
						println("smartBell | waiting for client requests")
					}
					 transition(edgeName="t038",targetState="checkTemp",cond=whenRequest("enter"))
					transition(edgeName="t039",targetState="answer",cond=whenReply("waitTimeAnswer"))
				}	 
				state("checkTemp") { //this:State
					action { //it:State
						updateResourceRep( "checkTemp"  
						)
						println("smartBell | checkTemp")
						if( checkMsgContent( Term.createTerm("enter(TEMP)"), Term.createTerm("enter(TEMP)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								solve("tempOk(${payloadArg(0)})","") //set resVar	
								if( currentSolution.isSuccess() ) {println("smartBell | waitermind")
								request("waitTime", "waitTime($ID)" ,"waitermind" )  
								}
								else
								{println("smartBell | client")
								answer("enter", "tempNotOk", "tempNotOK(OK)"   )  
								}
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("answer") { //this:State
					action { //it:State
						updateResourceRep( "answer"  
						)
						println("smartBell | answer")
						if( checkMsgContent( Term.createTerm("waitTime(TIME)"), Term.createTerm("waitTimeAnswer(TIME)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								answer("enter", "tempOk", "tempOk($ID)"   )  
						}
						
									ID = ID + 1
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}
