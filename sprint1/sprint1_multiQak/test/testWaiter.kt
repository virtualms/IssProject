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
 
class testWaiter {
	
var actor 			  = "waiter"
var robot             : ActorBasic? = null
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
	suspend fun forwardToRobot(msgId: String, payload:String){
		println(" --- forwardToRobot --- $msgId:$payload")
		if( robot != null )  MsgUtil.sendMsg( "test",msgId, payload, robot!!  )
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun emit(msgId: String, payload:String){
		println(" --- emit --- $msgId:$payload")
		if( robot != null )  {
			val event = MsgUtil.buildEvent( "test",msgId, payload)
			
			//cosi fa in ActorBasic.kt
			robot!!.actor.send(event)
			
		}
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun requestToRobot(msgId: String, payload:String){
		if( robot != null ){
			val msg = MsgUtil.buildRequest("test",msgId, payload,robot!!.name)
			MsgUtil.sendMsg( msg, robot!!  )		
		}  
	}
	
	fun checkResource(value: String){		
		if( robot != null ){
			println(" --- checkResource --- ${robot!!.geResourceRep()} value=$value")
			assertTrue( robot!!.geResourceRep() == value)
		}  
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testServing(){
		println("=========== testServing =========== ")
 			emit("teaReady", "teaReady(i)")
 			delay(300)
 			checkResource("serving")
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testEndClient(){
		println("=========== testEndClient =========== ")
 			emit("endCons", "endCons(i)")
 			delay(300)
 			checkResource("endClient")
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testNewClient(){ //ASSUMPTION: only one client
		println(" ===========  testNewClient =========== ")
			emit("newClient","newClient(i)")
			delay(200)
			checkResource("newClient") 
	}
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testOrder(){ 
		println(" ===========  testOrder  =========== ")
			emit( "callWaiter", "callWaiter(i)" )
			delay(300)
			checkResource("orderManagement")
 	}
 
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testWaiter(){
	 	runBlocking{
			while( robot == null ){
				delay(initDelayTime)  //time for robot to start
				robot = it.unibo.kactor.sysUtil.getActor(actor)				
			}
			delay( 1000 )
			checkResource("waiting")
			testNewClient()
			delay( 1000 )
			testOrder()
			delay( 1000 )
			testServing()
			delay( 1000 )
			testEndClient()
			
//			if( robot != null ) robot!!.waitTermination()
		}
	 	println("testWaiter BYE  ")  
	}

}