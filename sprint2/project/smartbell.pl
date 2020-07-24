%====================================================================================
% smartbell description   
%====================================================================================
context(ctxsmartbell, "localhost",  "TCP", "8050").
context(ctxwaiter, "127.0.0.1",  "TCP", "8090").
 qactor( waitermind, ctxwaiter, "external").
  qactor( smartbell, ctxsmartbell, "it.unibo.smartbell.Smartbell").
