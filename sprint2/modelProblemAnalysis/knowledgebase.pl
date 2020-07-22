teatable( 1, clean).
teatable( 2, clean).

stateOfTeatables( [table1(V1),table2(V2)] ) :-
	teatable( 1, V1 ),
	teatable( 2, V2 ).

numfreetables(N) :-
	findall( N,teatable( N,clean ), NList),
	length(NList,N).
	
takenTable(N)	 :-
	retract( teatable( N, clean ) ),
	!,
	assert( teatable( N, taken ) ).
takenTable(N).		
	
dirtyTable(N)	 :-
	retract( teatable( N, taken ) ),
	!,
	assert( teatable( N, dirty ) ).
dirtyTable(N).	

clearTable(N)	 :-
	retract( teatable( N, dirty ) ),
	!,
	assert( teatable( N, clear ) ).
clearTable(N).	

cleanTable(N)	 :-
	retract( teatable( N, clear ) ),
	!,
	assert( teatable( N, clean ) ).
cleanTable(N).	


