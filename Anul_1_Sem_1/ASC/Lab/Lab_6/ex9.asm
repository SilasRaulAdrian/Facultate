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
    sir_initial dd 12345678h, 1A2C3C4Dh, 98FCDD76h, 12783A2Bh 
    lungime_sir equ 4                                       
    sir_rezultat dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [sir_initial]
        mov ecx, lungime_sir
        xor edi, edi
        mov eax, 0FF000000h
        
    procesare_sir:
        lodsd
        mov bx, ax
        
        mov al, bh
        test al, 1
        jnz next_element
        
        shl edi, 8
        or edi, eax
        
    next_element:
        loop procesare_sir
        
        mov [sir_rezultat], edi
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
