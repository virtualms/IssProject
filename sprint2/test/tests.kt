package test 

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.MqttUtils
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import kotlinx.coroutines.channels.actor
 
 

class test {
	
var waiterbody             : ActorBasic? = null
var smartbell             : ActorBasic? = null
val mqttTest   	      = MqttUtils("test") 
val initDelayTime     = 1500L   // 
       
	@Before
	fun systemSetUp() {
   		kotlin.concurrent.thread(start = true) {
			it.unibo.ctxwaiter.main()
			it.unibo.ctxsmartbell.main()
		}
	}

	@After
	fun terminate() {
		println("%%%  Test terminate ")
	}
	
	//functions
	fun checkResource(value: String){		
		if( waiterbody != null ){
			println(" --- checkResource --- ${waiterbody!!.geResourceRep()}")
			assertTrue( waiterbody!!.geResourceRep() == value)
		}  
	}
	
	fun checkResourceFalse(value: String){		
		if( waiterbody != null ){
			println(" --- checkResource --- ${waiterbody!!.geResourceRep()}")
			assert( waiterbody!!.geResourceRep() != value)
		}  
	}
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun forwardToWaiter(msgId: String, payload:String){
		println(" --- forwardToRobot --- $msgId:$payload")
		MsgUtil.sendMsg( "test",msgId, payload, waiterbody!!)
	}
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun forwardToSmartbell(msgId: String, payload:String){
		println(" --- forwardToRobot --- $msgId:$payload")
		MsgUtil.sendMsg( "test",msgId, payload, smartbell!!)
	}
	
	@Test
    fun testSmartBell(){
		runBlocking{
			while( waiterbody == null ){
				delay(1000)
				waiterbody = it.unibo.kactor.sysUtil.getActor("waiterbody") 
			}
			
			while( smartbell == null ){
				delay(1000)
				smartbell = it.unibo.kactor.sysUtil.getActor("smartbell") 
			}
			
			delay(1000)
			forwardToSmartbell("enter", "38")
			delay(1000)
			checkResource("waiting")
			
			delay(1000)
			forwardToSmartbell("enter", "36")
			delay(1000)
			checkResourceFalse("waiting")
		}
	}
}