%====================================================================================
% smartbell description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/polar").
context(ctxwaiter, "127.0.0.1",  "TCP", "8090").
context(ctxsmartbell, "localhost",  "TCP", "8050").
 qactor( waiter, ctxwaiter, "external").
  qactor( smartbell, ctxsmartbell, "it.unibo.smartbell.Smartbell").
  qactor( client, ctxsmartbell, "it.unibo.client.Client").
