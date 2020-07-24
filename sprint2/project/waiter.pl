%====================================================================================
% waiter description   
%====================================================================================
context(ctxwaiter, "localhost",  "TCP", "8090").
context(ctxbarman, "localhost",  "TCP", "8060").
 qactor( barman, ctxbarman, "external").
  qactor( waitermind, ctxwaiter, "it.unibo.waitermind.Waitermind").
  qactor( waiterbody, ctxwaiter, "it.unibo.waiterbody.Waiterbody").
  qactor( walker, ctxwaiter, "it.unibo.walker.Walker").
  qactor( waiterwalker, ctxwaiter, "it.unibo.waiterwalker.Waiterwalker").
  qactor( basicrobot, ctxwaiter, "it.unibo.basicrobot.Basicrobot").
