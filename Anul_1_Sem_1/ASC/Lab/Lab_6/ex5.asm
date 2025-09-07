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
    sir1 db 7, 33, 55, 19, 46      
    lungime_sir1 equ 5                 
    sir2 db 33, 21, 7, 13, 27, 19, 55, 1, 46
    lungime_sir2 equ 9                 
    sir_rezultat times 9 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [sir2]
        lea edi, [sir_rezultat]
        mov ecx, lungime_sir2
        
    procesare_s2:
        lodsb
        xor ebx, ebx
        lea edx, [sir1]
        mov bl, lungime_sir1
        
    cautare_s1:
        mov bh, [edx]
        cmp al, bh
        je gasit
        inc edx
        dec bl
        jnz cautare_s1
        
        mov [edi], 0
        jmp next_s2
        
    gasit:
        mov eax, edx
        sub eax, sir1
        inc eax
        mov [edi], al
        
    next_s2:
        inc edi
        loop procesare_s2
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
