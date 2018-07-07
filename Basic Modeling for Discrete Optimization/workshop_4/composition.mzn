int: n;
array[1..n] of var 1..n: order;
include "alldifferent.mzn";
constraint alldifferent(order);
array[1..n-1] of var 1..n-1: difference = [abs(order[i] - order[i+1]) | i in 1..n-1];
constraint alldifferent(difference);
solve satisfy;

output ["\(order)\n \(difference)"];
