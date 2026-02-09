; nr-aparitii(lst, a) =
;   - daca lst este lista vida: 0
;   - daca lst este atom:
;         daca lst = a atunci 1
;         altfel 0
;   - altfel:
;         nr-aparitii(l1, a) + nr-aparitii(l2...ln, a)

(defun nr-aparitii (lst a)
  (cond
    ((null lst) 0)
    ((atom lst)
     (if (= lst a) 1 0))
    (t (+ (nr-aparitii (car lst) a)
          (nr-aparitii (cdr lst) a)))))
