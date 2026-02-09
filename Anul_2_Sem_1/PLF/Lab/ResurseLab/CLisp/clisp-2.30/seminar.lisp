; Sa se elimine toate aparitiile unui atom E dintr-o lista neliniara

(defun elim(lst e)
	(cond 
		((null lst) nil)
		((and (atom (car lst)) (not(equal (car lst) e)))
		(cons (car lst) (elim(cdr lst) e)))
		((listp (car lst)) (cons (elim(car lst) e) (elim(cdr lst) e)))
		(t (elim(cdr lst) e))))
		
; Se da o lista liniara. Sa se construiasca lista pozitiilor elementului minim.
; Indexaarea incepe de la 1
; ex: (a 1 2 3 1 5 a 1 b) => (2 5 8)

(defun minim(lst)
	(cond
		((null lst) most-positive-double-float)
		((numberp (car lst)) (min(car lst) (minim(cdr lst))))
		(t (minim(cdr lst)))
	)
)
		
(defun pozitii(lst elem cPoz)
	(cond
		((null lst) nil)
		((equal (car lst) elem) (cons cPoz (pozitii(cdr lst) elem (+ cPoz 1))))
		(t (pozitii (cdr lst) elem (+ cPoz 1)))
	)
)

(defun main(lst)
	(pozitii lst (minim lst) 1)
)

; Sa se interclaseze fara pastrarea dublurilor 2 liste liniare, numerice, sortate crescator
; a) listele initiale nu au dublurilor
; ex: (1 2 3 4) (1 3 4 10) => (1 2 3 4 10)
; b) listele initiale pot avea dublurilor
; ex: (1 2 2 2 3 4) (1 3 3 4 10) => (1 2 3 4 10) 

(defun 