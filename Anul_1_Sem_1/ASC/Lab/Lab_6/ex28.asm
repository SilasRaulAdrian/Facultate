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
    s db 1, 2, 3, 4, 5, 2, 3, 6, 2, 3, 7, 8 
    len_s equ ($ - s)                     

    subsir db 2, 3                        
    len_subsir equ ($ - subsir)           

    d times 16 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [s]
        lea edi, [d]
        mov ecx, len_s
        
        lea ebx, [subsir]
        mov edx, len_subsir
        
    parcurgere_sir:
        cmp ecx, edx
        jl copiere_restant
        
        push ecx
        push esi
        
        mov ecx, edx
        repe cmpsb
        je eliminare_subsir
        
        pop esi
        pop ecx
        
        mov al, [esi]
        mov [edi], al
        inc esi
        inc edi
        dec ecx
        jmp parcurgere_sir
        
    eliminare_subsir:
        pop esi
        pop ecx
        add esi, edx
        sub ecx, edx
        jmp parcurgere_sir
        
    copiere_restant:
        cmp ecx, 0
        je finalizare
        
        mov al, [esi]
        mov [edi], al
        inc esi
        inc edi
        dec ecx
        jmp copiere_restant
        
    finalizare:
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
