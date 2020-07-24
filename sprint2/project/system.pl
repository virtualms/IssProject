%====================================================================================
% system description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/polar").
context(ctxwaiter, "localhost",  "TCP", "8010").
 qactor( datacleaner, ctxwaiter, "rx.dataCleaner").
  qactor( distancefilter, ctxwaiter, "rx.distanceFilter").
  qactor( tearoom, ctxwaiter, "it.unibo.tearoom.Tearoom").
  qactor( timer1, ctxwaiter, "it.unibo.timer1.Timer1").
  qactor( timer2, ctxwaiter, "it.unibo.timer2.Timer2").
  qactor( waitermind, ctxwaiter, "it.unibo.waitermind.Waitermind").
  qactor( waiterbody, ctxwaiter, "it.unibo.waiterbody.Waiterbody").
  qactor( barman, ctxwaiter, "it.unibo.barman.Barman").
  qactor( smartbell, ctxwaiter, "it.unibo.smartbell.Smartbell").
  qactor( walker, ctxwaiter, "it.unibo.walker.Walker").
  qactor( waiterwalker, ctxwaiter, "it.unibo.waiterwalker.Waiterwalker").
  qactor( basicrobot, ctxwaiter, "it.unibo.basicrobot.Basicrobot").
