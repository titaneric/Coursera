int: n;
array[1..n] of var 1..n: order;
array[1..n-1] of var 1..n-1: diffs = [abs(order[i] - order[i+1]) | i in 1..n-1];
array[1..n] of var 1..n: posn;
array[1..n-1] of var 1..n-1: dposn;

include "inverse.mzn";
constraint inverse(order, posn);
constraint inverse(diffs, dposn);
constraint abs(posn[1] - posn[n]) = 1;
solve satisfy;

output ["order = \(order);\ndiffs = \(diffs);\nposn = \(posn);\ndposn = \(dposn);"];