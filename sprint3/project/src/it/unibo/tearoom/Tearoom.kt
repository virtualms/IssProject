/* Generated by AN DISI Unibo */ 
package it.unibo.tearoom

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Tearoom ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 
				var TABLE = 0 
				var TableState = ""
				var State = ""
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						discardMessages = false
						solve("consult('knowledgebase.pl')","") //set resVar	
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("tearoom | wait")
						solve("stateOfTeatables(S)","") //set resVar	
						if( currentSolution.isSuccess() ) { State = getCurSol("S").toString()  
						}
						else
						{}
						updateResourceRep( State  
						)
					}
					 transition(edgeName="t00",targetState="waitTime",cond=whenRequest("calculateTime"))
					transition(edgeName="t01",targetState="tableToClean",cond=whenRequest("tableToClean"))
					transition(edgeName="t02",targetState="checkTableState",cond=whenRequest("checkTableState"))
					transition(edgeName="t03",targetState="dirty",cond=whenRequest("updateDirty"))
					transition(edgeName="t04",targetState="clear",cond=whenRequest("updateClear"))
					transition(edgeName="t05",targetState="sanitize",cond=whenRequest("updateSanitize"))
				}	 
				state("waitTime") { //this:State
					action { //it:State
						println("tearoom | waitTime")
						solve("teatable(X,clean)","") //set resVar	
						if( currentSolution.isSuccess() ) {solve("teatable(X,clean)","") //set resVar	
						if( currentSolution.isSuccess() ) { 
												TABLE = getCurSol("X").toString().toInt()
						solve("takenTable($TABLE)","") //set resVar	
						answer("calculateTime", "time", "time(0,$TABLE)"   )  
						}
						else
						{}
						}
						else
						{solve("teatable(X,dirty)","") //set resVar	
						if( currentSolution.isSuccess() ) {answer("calculateTime", "time", "time(5000,0)"   )  
						}
						else
						{solve("teatable(X,taken)","") //set resVar	
						if( currentSolution.isSuccess() ) {answer("calculateTime", "time", "time(10000,0)"   )  
						}
						else
						{}
						}
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("tableToClean") { //this:State
					action { //it:State
						println("tearoom | tableToClean")
						solve("teatable(1,dirty)","") //set resVar	
						if( currentSolution.isSuccess() ) {answer("tableToClean", "tableToClear", "tableToClear(1)"   )  
						}
						else
						{solve("teatable(2,dirty)","") //set resVar	
						if( currentSolution.isSuccess() ) {answer("tableToClean", "tableToClear", "tableToClear(2)"   )  
						}
						else
						{solve("teatable(1,clear)","") //set resVar	
						if( currentSolution.isSuccess() ) {answer("tableToClean", "tableToSanitize", "tableToSanitize(1)"   )  
						}
						else
						{solve("teatable(2,clear)","") //set resVar	
						if( currentSolution.isSuccess() ) {answer("tableToClean", "tableToSanitize", "tableToSanitize(2)"   )  
						}
						else
						{}
						}
						}
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("checkTableState") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("checkTableState(TABLE)"), Term.createTerm("checkTableState(TABLE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												TABLE = payloadArg(0).toString().toInt()
								solve("teatable($TABLE,X)","") //set resVar	
								if( currentSolution.isSuccess() ) { TableState = getCurSol("X").toString()  
								}
								else
								{}
								answer("checkTableState", "tableState", "tableState($TableState)"   )  
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("dirty") { //this:State
					action { //it:State
						println("tearoom | dirty")
						if( checkMsgContent( Term.createTerm("updateDirty(TABLE)"), Term.createTerm("updateDirty(TABLE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												TABLE = payloadArg(0).toString().toInt()
								solve("dirtyTable($TABLE)","") //set resVar	
								answer("updateDirty", "dirtyUpdated", "dirtyUpdated(OK)"   )  
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("clear") { //this:State
					action { //it:State
						println("tearoom |clear")
						if( checkMsgContent( Term.createTerm("updateClear(TABLE)"), Term.createTerm("updateClear(TABLE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												TABLE = payloadArg(0).toString().toInt()
								solve("clearTable($TABLE)","") //set resVar	
								answer("updateClear", "clearUpdated", "clearUpdated(OK)"   )  
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("sanitize") { //this:State
					action { //it:State
						println("tearoom | sanitize")
						if( checkMsgContent( Term.createTerm("updateSanitize(TABLE)"), Term.createTerm("updateSanitize(TABLE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												TABLE = payloadArg(0).toString().toInt()
								solve("cleanTable($TABLE)","") //set resVar	
								answer("updateSanitize", "sanitizeUpdated", "sanitizeUpdated(OK)"   )  
						}
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}
