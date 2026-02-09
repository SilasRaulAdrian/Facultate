; adancime(arbore) = | 1,                                             daca arbore = (radacina)
;                    | 1 + max( adancime(subarbore1),
;                              adancime(subarbore2),
;                              ...
;                              adancime(subarboreN) ),                altfel
;
; unde arbore = (radacina subarbore1 subarbore2 ... subarboreN)

(defun adancime (arbore)
  (if (null (cdr arbore))
      1
      (+ 1 (apply #'max
                  (mapcar #'adancime (cdr arbore))))))
