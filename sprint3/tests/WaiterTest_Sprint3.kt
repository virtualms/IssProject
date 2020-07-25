package test
 	//"tcp://mqtt.eclipse.org:1883"
	//mqtt.eclipse.org
	//tcp://test.mosquitto.org
	//mqtt.fluux.io
	//"tcp://broker.hivemq.com"


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
 
class WaiterTest {
	
var actor 			  = "waiterbody"
var waiter 			  : ActorBasic? = null
var teaRoom             : ActorBasic? = null
val mqttTest   	      = MqttUtils("test") 
val initDelayTime     = 1000L   // 
val useMqttInTest 	  = false
val mqttbrokerAddr    = "localhost"//"tcp://broker.hivemq.com" 
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() { 
		
   		kotlin.concurrent.thread(start = true) {
			
			//todo
			it.unibo.ctxwaiter.main()
			
			println("testWaiter systemSetUp done")
   			if( useMqttInTest ){
				 while( ! mqttTest.connectDone() ){
					  println( "	attempting MQTT-conn to ${mqttbrokerAddr}  for the test unit ... " )
					  Thread.sleep(1000)
					  mqttTest.connect("test_teaRoom", mqttbrokerAddr )					 
				 }
 			}	
 	} 
}	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@After
	fun terminate() {
		println("testWaiter terminated ")
	}
	
 	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun forwardToRobot(msgId: String, payload:String, robot:ActorBasic?){
		println(" --- forwardToRobot --- $msgId:$payload")
		if( robot != null )  MsgUtil.sendMsg( "test",msgId, payload, robot!!  )
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun emit(msgId: String, payload:String, robot:ActorBasic?){
		println(" --- emit --- $msgId:$payload")
		if( robot != null )  {
			val event = MsgUtil.buildEvent( "test",msgId, payload)
			
			//cosi fa in ActorBasic.kt
			robot!!.actor.send(event)
			
		}
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun requestToRobot(msgId: String, payload:String, robot:ActorBasic?){
		if( robot != null ){
			val msg = MsgUtil.buildRequest("test",msgId, payload,robot!!.name)
			MsgUtil.sendMsg( msg, robot!!  )		
		}  
	}
	
	fun checkResource(value: String, robot:ActorBasic?){		
		if( robot != null ){
			println(" --- checkResource --- ${robot!!.geResourceRep()} value=$value")
			assertTrue( robot!!.geResourceRep() == value)
		}  
	}
	
	fun checkResourcePattern(pattern: String, robot:ActorBasic?){		
		if( robot != null ){
			println(" --- checkResourcePatter --- ${robot!!.geResourceRep()} with pattern $pattern")
			assertTrue(robot!!.geResourceRep().matches(Regex(pattern)))
		}  
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testEnd(){
		println("=========== testEnd =========== ")
			checkResourcePattern("((.*?)clean(.*?)){2}", teaRoom) //to check the presence of 2 clean tables after the clients
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testNewClient(){
		println(" ===========  testNewClient =========== ")
			checkResourcePattern("((.*?)clean(.*?)){2}", teaRoom) //to check the presence of 2 clean tables
			
			forwardToRobot("newClient","newClient(1,1)", waiter)

			delay( 7500 )

			checkResourcePattern("((.*?)clean(.*?)){1}", teaRoom) //to check the presence of 1 clean table
			checkResourcePattern("((.*?)taken(.*?)){1}", teaRoom) //and 1 taken table
	}
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testOrder(){ 
		println(" ===========  testOrder  =========== ")
	
			requestToRobot( "readyToOrder", "readyToOrder(1)", waiter)

			delay(7500)

			checkResource("takeOrder 1", waiter)
	
			forwardToRobot( "order", "order(3, 1)", waiter)
	
			delay(1000)

			requestToRobot( "readyToPay", "readyToPay(1)", waiter)
			delay(20000)
	
			checkResource("collect 1", waiter) //managing the exit of the client
	
			forwardToRobot( "moneyCollected", "moneyCollected(5)", waiter)
	
			checkResourcePattern("((.*?)dirty(.*?)){1}", teaRoom) //to check the presence of 1 dirty table
			checkResourcePattern("((.*?)clean(.*?)){1}", teaRoom) //and 1 taken table

			delay(4000)
			
			checkResourcePattern("((.*?)clear(.*?)){1}", teaRoom) //to check the presence of 1 clear table
			checkResourcePattern("((.*?)clean(.*?)){1}", teaRoom) //to check the presence of 1 clean table
			
			delay(10000)
			
			checkResourcePattern("((.*?)clean(.*?)){2}", teaRoom) //to check the presence of 1 clean table
			
 	}
 
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testWaiter(){
	 	runBlocking{
			while( waiter == null ){
				delay(initDelayTime)  //time for waiter to start
				waiter = it.unibo.kactor.sysUtil.getActor(actor)				
			}
			while( teaRoom == null ){
//				delay(initDelayTime)  
				teaRoom = it.unibo.kactor.sysUtil.getActor("teaRoom")				
			}
			
			delay( 1500 )
			checkResource("home", waiter)
			
			//test 1 client
			testNewClient()
		
			delay( 1000 )
			
			//test the table cleaning
			testOrder()
			
			delay( 1000 )
			
			testEnd()
			
//			if( robot != null ) robot!!.waitTermination()
		}
	 	println("testWaiter BYE  ")  
	}

}