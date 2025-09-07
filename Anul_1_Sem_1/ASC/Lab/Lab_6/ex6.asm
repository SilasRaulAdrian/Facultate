bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    sir_initial dw -22, 145, -48, 127    
    lungime_sir equ 4                   
    sir_rezultat times 4 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [sir_initial]
        lea edi, [sir_rezultat]
        mov ecx, lungime_sir
        
    procesare_sir:
        lodsw
        
        test ax, ax
        js negativ
        
    pozitiv:
        mov bx, ax
        xor dl, dl
        mov dh, 16
        
    contor_biti_1:
        test bx, 1
        jz skip_incr_1
        inc dl
    skip_incr_1:
        shr bx, 1
        dec dh
        jnz contor_biti_1
        
        mov [edi], dl
        jmp next_cuvant
        
    negativ:
        not ax
        mov bx, ax
        xor dl, dl
        mov dh, 16
        
    contor_biti_0:
        test bx, 1
        jz skip_incr_0
        inc dl
    skip_incr_0:
        shr bx, 1
        dec dh
        jnz contor_biti_0
        
        mov [edi], dl
        
    next_cuvant:
        inc edi
        loop procesare_sir
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
