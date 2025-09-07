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
    sir1 db 3, 7, 12, 25, 9, 4         
    len_sir1 equ ($ - sir1)            

    sir2 db 8, 5, 14, 6                
    len_sir2 equ ($ - sir2)           

    output times 16 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [sir1]
        lea edi, [sir2]
        lea ebx, [output]
        
        mov ecx, len_sir1
        cmp len_sir1, len_sir2
        jl set_min_len
        mov ecx, len_sir2
        
    set_min_len:
        xor edx, edx
        
    parcurgere:
        cmp ecx, 0
        je completare
        
        mov al, [esi]
        mov dl, [edi]
        cmp al, dl
        jae adauga_sir1
        mov [ebx], dl
        jmp avansare
        
    adauga_sir1:
        mov [ebx], al
        
    avansare:
        inc esi
        inc edi
        inc ebx
        dec ecx
        jmp parcurgere
        
    completare:
        mov ecx, len_sir1
        cmp len_sir1, len_sir2
        jbe set_sir2_len
        mov ecx, len_sir2
        
    set_sir2_len:
        sub ecx, edx
        xor edx, edx
        
    adaugare_rest:
        cmp ecx, 0
        je finalizare
        
        test edx, 1
        jz adauga_zero
        mov byte [ebx], 1
        jmp avanseaza_completare
        
    adauga_zero:
        mov byte [ebx], 0
        
    avanseaza_completare:
        inc ebx
        inc edx
        dec ecx
        jmp adaugare_rest
        
    finalizare:
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
