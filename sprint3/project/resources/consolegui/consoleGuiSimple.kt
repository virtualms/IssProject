package consolegui

import connQak.connQakBase
import it.unibo.`is`.interfaces.IObserver
import java.util.Observable
import connQak.ConnectionType
import it.unibo.kactor.MsgUtil

class consoleGuiSimple( val connType : ConnectionType, val hostIP : String,   val port : String,
						val destName : String ) : IObserver {
lateinit var connQakSupport : connQakBase
	
	 val buttonLabels = arrayOf("Suono Campanello","Voglio Ordinare Tavolo 1", "Voglio Ordinare Tavolo 2",  "Ordino Tavolo 1",
			 					"Ordino Tavolo 2", "Voglio Pagare Tavolo 1", "Voglio Pagare Tavolo 2", "Pago")
 
	
	init{
		create( connType )
	}
	
	 fun create( connType : ConnectionType){
		 connQakSupport = connQakBase.create(connType, hostIP, port,destName )
		 connQakSupport.createConnection()
		 var guiName = ""
		 when( connType ){
			 ConnectionType.COAP -> guiName="GUI-COAP"
			 ConnectionType.MQTT -> guiName="GUI-MQTT"
			 ConnectionType.TCP  -> guiName="GUI-TCP"
			 ConnectionType.HTTP -> guiName="GUI-HTTP"
		 }
		 createTheGui( guiName )		 
	  }
	  fun createTheGui( guiName : String ){
  			val concreteButton = ButtonAsGui.createButtons( guiName, buttonLabels )
            concreteButton.addObserver( this )		  
	  }
	 
	
	  
	  override fun update(o: Observable, arg: Any) {	   
    	   var move = arg as String
  		  if( move.equals("Voglio Ordinare Tavolo 1", true) ){
			  val msg = MsgUtil.buildRequest("console","readyToOrder","readyToOrder(1)", destName )
			  connQakSupport.request( msg , "waiterbody")
		  }
		  else if( move.equals("Voglio Ordinare Tavolo 2", true) ){
			  val msg = MsgUtil.buildRequest("console","readyToOrder","readyToOrder(2)", destName )
			  connQakSupport.request( msg , "waiterbody")
		  }
		  else if( move.equals("Ordino Tavolo 1", true) ){
			  val msg = MsgUtil.buildDispatch("console","order","order(3, 1)", destName )
			  connQakSupport.forward( msg, "waiterbody" )
		  }
  		  else if( move.equals("Ordino Tavolo 2", true) ){
			  val msg = MsgUtil.buildDispatch("console","order","order(3, 2)", destName )
			  connQakSupport.forward( msg, "waiterbody" )
		  }
		  else if( move.equals("Voglio Pagare Tavolo 1", true) ){
			  val msg = MsgUtil.buildRequest("console","readyToPay","readyToPay(1)", destName )
			  connQakSupport.request( msg, "waiterbody" )
		  }
  		  else if( move.equals("Voglio Pagare Tavolo 2", true) ){
			  val msg = MsgUtil.buildRequest("console","readyToPay","readyToPay(2)", destName )
			  connQakSupport.request( msg, "waiterbody" )
		  }
  		  else if( move.equals("Pago", true) ){
			  val msg = MsgUtil.buildDispatch("console","moneyCollected","moneyCollected(5)", destName )
			  connQakSupport.forward( msg, "waiterbody" )
		  }
  		  else 	if( move.equals("Suono Campanello", true) ){
			  
			  val msg = MsgUtil.buildRequest("console", "enter", "enter(37)", "smartbell" )
			  
			  connQakSupport.request( msg , "smartbell" )
		  }
       }//update
	
}


fun main(){
	consoleGuiSimple( ConnectionType.COAP, hostAddr, port, qakdestinationTable)
}