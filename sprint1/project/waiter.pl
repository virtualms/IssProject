%====================================================================================
% waiter description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/polar").
context(ctxwaiter, "localhost",  "TCP", "8090").
context(ctxsmartbell, "127.0.0.1",  "TCP", "8050").
 qactor( client, ctxsmartbell, "external").
  qactor( smartbell, ctxsmartbell, "external").
  qactor( datacleaner, ctxwaiter, "rx.dataCleaner").
  qactor( distancefilter, ctxwaiter, "rx.distanceFilter").
  qactor( waiter, ctxwaiter, "it.unibo.waiter.Waiter").
  qactor( walker, ctxwaiter, "it.unibo.walker.Walker").
  qactor( waiterwalker, ctxwaiter, "it.unibo.waiterwalker.Waiterwalker").
  qactor( basicrobot, ctxwaiter, "it.unibo.basicrobot.Basicrobot").
