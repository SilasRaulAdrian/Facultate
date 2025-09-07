bits 32

segment code use32 public code
global prim

prim:
    mov eax, [esp + 4] 
    mov ebx, 1  ; ebx = 1 => nr prim; ebx = 0 => nr nu e prim;
    mov ecx, 2  ; iteratorul la care vom imparti pe rand
    
    cmp eax, ecx
    jl neprim       ; pt cazurile 0 si 1
    
    repeta:
        mov eax, [esp + 4]  ;restauram eax dupa impartirea anterioara
        cmp ecx, eax
        je final            ;daca am terminat de iterat si nu am gasit un
                            ;divizor atunci nr este prim
        mov dx, 0
        div cx      ; dx:ax / cx 
        cmp dx, 0   ; dx = dx:ax % cx
        je neprim   ; daca e divizibil cu cx atunci nu e prim
        
        inc ecx     ; incrementam iteratorul
        jmp repeta
        
    neprim:
        mov ebx, 0  
        
    final:
        ret 4
        