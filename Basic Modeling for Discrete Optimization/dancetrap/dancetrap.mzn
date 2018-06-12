% Beauty Trap
% Perform the most seductive dance

enum LEGS = {spin, leap, waltz, curtsey, prep, stand};
enum ARMS = {beckon, out, up, wrapped, neutral};
enum FACE = {smile, wink, batt, think, glow, blank};

int: maxlen;
set of int: STEP = 1..maxlen;
array[LEGS] of int: maxlegs;
array[ARMS] of int: maxarms;
array[FACE] of int: maxface;
constraint assert(maxlegs[stand] >= maxlen, "maxlegs[stand] smaller than maxlen");
constraint assert(maxarms[neutral] >= maxlen, "maxarms[neutral] smaller than maxlen");
constraint assert(maxface[blank] >= maxlen, "maxface[blank] smaller than maxlen");
array[LEGS,ARMS] of int: dance_value;
array[ARMS,FACE] of int: entice_value;
constraint assert(dance_value[stand,neutral] = 0, "incorrect dance_value array");
constraint assert(entice_value[neutral,blank] = 0, "incorrect entice_value array");
int: boredom; % how bored each step make the viewer

var STEP: len;
array[STEP] of var LEGS: legs;
array[STEP] of var ARMS: arms;
array[STEP] of var FACE: face;

include "regular.mzn";
include "atmost.mzn";

% LEGS constraint

constraint regular(legs, 7, 6, legs_transition, 1, 1..7);
array[1..7, LEGS] of 0..7: legs_transition = 
 [| 2, 0, 4, 5, 6, 7    % Start 
  | 0, 0, 0, 5, 6, 7    % Spin
  | 2, 0, 4, 0, 0, 7    % Leap
  | 2, 0, 4, 5, 6, 7    % Waltz
  | 2, 0, 4, 5, 0, 7    % Curtsey
  | 2, 3, 4, 5, 6, 7    % Prep
  | 2, 0, 4, 5, 6, 7|]; % Stand

constraint forall(i in 1..maxlen-3)(
  sum(x in i..i+3)(legs[x] = waltz) <= 3
);
constraint forall(i, j in 1..maxlen where i < j) ((legs[i] = waltz /\ legs[j] = curtsey) -> exists(k in i+1..j-1)(legs[k] = stand));

% ARMS constraint

constraint regular(arms, 6, 5, arms_transition, 1, 1..6);
array[1..6, ARMS] of 0..6: arms_transition = 
[| 0, 3, 4, 5, 6       % Start
 | 0, 3, 4, 5, 6       % Beckon
 | 2, 0, 4, 0, 6       % Out
 | 2, 3, 0, 0, 6       % Up
 | 0, 3, 4, 0, 6       % Wrapped
 | 0, 3, 4, 5, 6 |];   % Neutral

constraint forall(i in 1..maxlen-1)(
  (arms[i] = arms[i+1]) ->  arms[i] = neutral
);

% FACE constraint

constraint regular(face, 7, 6, face_transition, 1, 1..7);
array[1..7, FACE] of 0..7: face_transition = 
[| 2, 3, 4, 5, 6, 7      % Start
 | 2, 3, 4, 0, 6, 7      % Smile
 | 2, 3, 4, 5, 6, 7      % Wink
 | 2, 3, 4, 0, 6, 7      % Batt
 | 2, 3, 4, 5, 6, 7      % Think
 | 2, 3, 4, 5, 6, 7      % Glow
 | 2, 3, 4, 5, 6, 7 |];  % Blank

constraint forall(i in 1..maxlen-2)(
  (face[i] = face[i+1] /\ face[i+1] = face[i+2]) -> face[i] = blank
);


% other constraint

constraint forall(x in LEGS)( at_most(maxlegs[x], legs, x ));
constraint forall(x in ARMS)( at_most(maxarms[x], arms, x ));
constraint forall(x in FACE)( at_most(maxface[x], face, x ));

constraint forall(i in len+1..maxlen)(legs[i] = stand /\ arms[i] = neutral /\ face[i] = blank);

% target

var int: value_enticement = sum(i in 1..len)(dance_value[legs[i], arms[i]] + entice_value[arms[i], face[i]]);
var int: obj = value_enticement - boredom * len;

solve maximize obj;
output ["len = \(len);\nlegs = \(legs);\narms = \(arms);\nface = \(face);\nobj = \(obj);"]
