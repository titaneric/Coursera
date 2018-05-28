array[int] of int: readings;
int: start;

array[0..length(readings)] of var int: temp;

constraint temp[0] = start;
constraint forall(t in 1..length(readings))(temp[t] = (temp[t-1] + readings[t]) div 2);

solve satisfy;
output ["temp = \(temp)"];