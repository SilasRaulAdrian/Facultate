; atomi-invers(lst) =
;   - daca lst este lista vida: NIL
;   - daca lst este atom: (lst)
;   - altfel:
;   	atomi-invers(l2...ln) (+)
;   	atomi-invers(l1)

(defun atomi-invers (lst)
  (cond
    ((null lst) nil)
    ((atom lst) (list lst))
    (t (append (atomi-invers (cdr lst))
               (atomi-invers (car lst))))))
