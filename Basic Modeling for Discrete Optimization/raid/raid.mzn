% assignment 2
% select a set of raiding warrior
enum WARRIOR;
WARRIOR: dummy; 
enum CLAN; % the clan they belong too

int: l; % minimum raid party size
int: u; % maximum raid party size
int: m; % limit of hatreds

array[WARRIOR] of int: strength;
array[WARRIOR] of CLAN: clan;

array[CLAN,CLAN] of 0..1: hates;

% set version
%var set of WARRIOR: raid;

%constraint not(dummy in raid);
%constraint card(raid) >= l;
%constraint card(raid) <= u;

%constraint sum(i,j in raid where i < j)(hates[clan[i],clan[j]]) <= m;

%var int: obj;
%constraint obj = sum([strength[i] | i in WARRIOR where i in raid]);
%solve maximize obj;
%output ["raid = \(raid)\nobj = \(obj)"];

% array version
array[1..u] of var WARRIOR: raid;
constraint forall(i in 1..u-1)(raid[i] >= raid[i+1] + (raid[i] != dummy));
constraint forall(i in 1..l)(raid[i] != dummy);

constraint sum(i,j in raid where i < j)(hates[clan[i],clan[j]]) <= m;
var int: obj;
constraint obj = sum([strength[i] | i in raid]);
solve maximize obj;
output ["raid = {"] ++ [if fix(raid[i]) != dummy then show(raid[i])
++ if i < u /\ fix(raid[i+1]) != dummy then ", " else "" endif
else "" endif | i in 1..u ] ++ ["};\nobj = \(obj);"];


