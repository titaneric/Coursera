array[int] of int: readings;
int: start;

set of int: TIME = 1..length(readings);
set of int: TIME0 = 0..length(readings);

enum ACTION = {heat, strongly_heat, cool, strongly_cool, do_nothing};
array[ACTION] of int: adjust = [+1, +4, -2, -5, 0];
array[ACTION] of int: c = [1, 5, 3, 9, 0];

array[TIME0] of var 25..30: temp;
array[TIME] of var ACTION: choice;
array[TIME] of var int: cost;

constraint temp[0] = start;
constraint forall(t in TIME)(temp[t] = (temp[t-1] + readings[t]) div 2 + adjust[choice[t]]);
constraint forall(t in TIME)(cost[t] = c[choice[t]]);
var int: total = sum(cost);

solve satisfy;
output ["temp = \(temp)\nchoice = \(choice)\ncost = \(total)"];
