; colecteaza-numere(lst) =
;   - daca lst este lista vida: NIL
;   - daca lst este un numar: (lst)
;   - daca lst este atom nenumeric: NIL
;   - altfel:
;         colecteaza-numere(l1) (+) colecteaza-numere(l2...ln)


; gcd-list(numbers) =
;   - daca lista este vida: 0
;   - daca len(lst) = 1: lst
;   - altfel:
;         gcd(l1, gcd-list(l2...ln))


; gcd-neliniar(lst) =
;   - gcd-list(aplica colecteaza-numere pe lst)

(defun colecteaza-numere (lst)
  (cond
    ((null lst) nil)
    ((numberp lst) (list lst))
	((atom lst) nil)
    (t (append (colecteaza-numere (car lst))
               (colecteaza-numere (cdr lst))))))
               
(defun gcd-list (numbers)
  (cond
    ((null numbers) 0)
    ((null (cdr numbers)) (car numbers))
    (t (gcd (car numbers) 
            (gcd-list (cdr numbers))))))
            
(defun gcd-neliniar (lst)
  (gcd-list (colecteaza-numere lst)))
