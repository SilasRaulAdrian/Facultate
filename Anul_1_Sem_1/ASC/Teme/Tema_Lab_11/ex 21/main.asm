bits 32

global start

extern prim
extern printf, scanf, exit
import printf msvcrt.dll
import scanf msvcrt.dll
import exit msvcrt.dll

segment data use32 class=data
    format db '%d', 0
    format2 db '%d', ' ', 0
    a dd 0
    
    ;Se citeste de la tastatura un sir de numere in baza 10. Sa se afiseze numerele prime.
    
segment code use32 class=code
start:

    repeta:
        ;scanf(format, &a)
        push dword a
        push dword format
        call [scanf]
        add esp, 4*2
        
        cmp dword[a], 0     ; 0 semnifica finalul sirului
        je final      
        
        mov eax, 0
        mov ax, [a]
        
        push eax
        call prim          ; verificam daca numarul citit e prim
              
        cmp ebx, 1          ; daca ebx = 1 atunci e prim si il afisam
        je e_prim
        
        jmp repeta          ; repetam pana il gasim pe 0
        
        e_prim:
            ;printf(format, a)
            push dword[a]
            push dword format2
            call [printf]
            add esp, 4*2
            jmp repeta
            
    final:
    
    push dword 0
    call [exit]
    