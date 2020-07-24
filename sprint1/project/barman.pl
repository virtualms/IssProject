%====================================================================================
% barman description   
%====================================================================================
mqttBroker("localhost", "1883", "unibo/polar").
context(ctxbarman, "localhost",  "TCP", "8060").
 qactor( barman, ctxbarman, "it.unibo.barman.Barman").
