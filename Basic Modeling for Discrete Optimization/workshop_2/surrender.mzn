% workshop 2
% select a set of trade negotiation party
enum NEGOTIATOR;
NEGOTIATOR: dummy; 

int: l; % minimum party size
int: u; % maximum party size
int: m; % minimum joint ability

array[NEGOTIATOR] of int: honor;
array[NEGOTIATOR,NEGOTIATOR] of int: joint;
% set version
%var set of NEGOTIATOR: party;
%
%constraint not(dummy in party);
%constraint card(party) >= l;
%constraint card(party) <= u;
%
%constraint sum(i,j in party where i < j)(joint[i,j]) >= m;
%var int: minhonor;
%constraint minhonor = min([honor[i] | i in NEGOTIATOR where i in party]);
%
%solve maximize minhonor;
%
%output ["party = \(party)\nobj = \(minhonor)"]

array[1..u] of var NEGOTIATOR: party;
constraint forall(i in 1..u-1)(party[i] >= party[i+1] + (party[i] != dummy));
constraint forall(i in 1..l)(party[i] != dummy);

constraint sum(i,j in party where i < j)(joint[i,j]) >= m;
var int: obj;
constraint obj = min([honor[i] | i in party]);
solve maximize obj;
output ["raid = {"] ++ [if fix(party[i]) != dummy then show(party[i])
++ if i < u /\ fix(party[i+1]) != dummy then ", " else "" endif
else "" endif | i in 1..u ] ++ ["};\nobj = \(obj)"];


