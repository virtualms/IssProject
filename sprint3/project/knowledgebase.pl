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

dirtyTobusy(N)	 :-
	retract( teatable( N, dirty ) ),
	!,
	assert( teatable( N, toClear ) ).
dirtyTobusy(N).	

clearTable(N)	 :-
	retract( teatable( N, toClear ) ),
	!,
	assert( teatable( N, clear ) ).
clearTable(N).	

clearTobusy(N)	 :-
	retract( teatable( N, clear ) ),
	!,
	assert( teatable( N, toSanitize ) ).
clearTobusy(N).	

cleanTable(N)	 :-
	retract( teatable( N, toSanitize ) ),
	!,
	assert( teatable( N, clean ) ).
cleanTable(N).	


