/* Generated by AN DISI Unibo */ 
package it.unibo.timer2

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Timer2 ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 var Timer = 30000L  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						discardMessages = true
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
				state("wait") { //this:State
					action { //it:State
						println("timer2 | wait")
					}
					 transition(edgeName="t09",targetState="timer",cond=whenDispatch("startTimer"))
				}	 
				state("timer") { //this:State
					action { //it:State
						println("timer2 | timer")
						stateTimer = TimerActor("timer_timer", 
							scope, context!!, "local_tout_timer2_timer", Timer )
					}
					 transition(edgeName="t010",targetState="timeover",cond=whenTimeout("local_tout_timer2_timer"))   
					transition(edgeName="t011",targetState="wait",cond=whenDispatch("stopTime"))
				}	 
				state("timeover") { //this:State
					action { //it:State
						println("timer2 | timerover")
						request("readyToPay", "readyToPay(2)" ,"waiterbody" )  
					}
					 transition( edgeName="goto",targetState="wait", cond=doswitch() )
				}	 
			}
		}
}
