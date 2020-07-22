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
 
class testBarman {
	
var actor 			  = "barman"
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
			it.unibo.ctxbarman.main()
			
			
			println("testBarman systemSetUp done")
			//robot = basicrobot("basicrobot", GlobalScope, usemqtt=useMqttInTest )
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
		println("testBarman terminated ")
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
	suspend fun testMakingTea(){ 
		println(" ===========  testMakingTea  =========== ")
			emit( "makeTea", "makeTea(i)" )
			delay(300)
			checkResource("makingTea")
 	}
 
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testBarman(){
	 	runBlocking{
			while( robot == null ){
				delay(initDelayTime)  //time for robot to start
				robot = it.unibo.kactor.sysUtil.getActor(actor)				
			}
			delay( 1000 )
			checkResource("waiting")
			delay( 1000 )
			testMakingTea()
			
//			if( robot != null ) robot!!.waitTermination()
		}
	 	println("testBarman BYE  ")  
	}

}