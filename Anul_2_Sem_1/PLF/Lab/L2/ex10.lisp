; nivel-nod-aux(l1,l2...ln, x, nv) = | [], 													        daca n = 0
; 								     | nv, 													        daca l1 = x
;								     | nivel-nod-aux(l2, x, nv + 1) v nivel-nod-aux(l3, x, nv + 1), altfel


(defun nivel-nod (arb x)
	(nivel-nod-aux arb x 0))
	
(defun nivel-nod-aux (arb x nivel)
	(cond
		((null arb) nil)
		((equal (car arb) x) nivel)
		(t
			(or (nivel-nod-aux (cadr arb) x (+ nivel 1))
				(nivel-nod-aux (caddr arb) x (+ nivel 1))))))