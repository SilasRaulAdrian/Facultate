; insereaza-dupa-pare(lst, a, poz) =
;   - daca lst este lista vida: NIL
;   - daca poz este par:
;         l1 (+) (a + insereaza-dupa-pare(l2...ln, a, poz + 1))
;   - altfel:
;         l1 (+) insereaza-dupa-pare(l2...ln, a, poz + 1)

;
;f(l1...ln,e,poz)=| [], n = 0
;				  | l1 (+) (e + f(l2...ln, e, poz + 1), poz % 2 = 0
;				  | l1 (+) f(l2...ln, e, poz + 1), poz % 2 = 1
;

(defun insereaza-dupa-pare (lst a poz)
  (cond
    ((null lst) nil)
    ((= (mod poz 2) 0)
     (cons (car lst)
           (cons a (insereaza-dupa-pare (cdr lst) a (+ poz 1)))))
    (t
     (cons (car lst)
           (insereaza-dupa-pare (cdr lst) a (+ poz 1))))))
