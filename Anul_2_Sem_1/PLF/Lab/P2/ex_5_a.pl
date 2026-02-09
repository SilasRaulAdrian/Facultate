% maxim(l1...ln) =
%     l1,                               daca n = 1
%     l1,                               daca maxim(l2...ln) = M1 si l1 >= M1
%     M1,                               daca maxim(l2...ln) = M1 si l1 < M1

% poz_aux(l1...ln, M, I) =
%     [],                                daca lista e vida
%     I + poz_aux(l2...ln, M, I+1),      daca l1 = M
%     poz_aux(l2...ln, M, I+1),          altfel

% pozitii_max(L) = poz_aux(L, maxim(L), 1)

% inlocuieste(l1...ln) =
%     [],                                     daca lista e vida
%     pozitii_max(l1) + inlocuieste(l2...ln), daca l1 e lista
%     l1 + inlocuieste(l2...ln),              altfel


% a)
% (i, o), (i, i)
maxim([H], H).
maxim([H|T], H) :-
    maxim(T, M1),
    H >= M1, !.
maxim([_|T], M) :-
    maxim(T, M).

% (i, o), (i, i)
pozitii_max(L, Poz):-
	maxim(L, M),
	poz_aux(L, M, 1, Poz).

% (i, i, o, o), (i, i, i, i), (i, i, i, o), (i, i, o, i)
poz_aux([], _, _, []).
poz_aux([H|T], M, I, [I|Rez]):-
	H =:= M, !,
	I1 is I + 1,
	poz_aux(T, M, I1, Rez).
poz_aux([_|T], M, I, Rez):-
	I1 is I + 1,
	poz_aux(T, M, I1, Rez).

% b)
% (i, o), (i, i)
inlocuieste([], []).
inlocuieste([[H|T]|Rest], [Poz|Rez]):-
	pozitii_max([H|T], Poz),
	inlocuieste(Rest, Rez).
inlocuieste([H|Rest], [H|Rez]):-
	inlocuieste(Rest, Rez).















