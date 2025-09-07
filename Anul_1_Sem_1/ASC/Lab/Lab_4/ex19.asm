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
    a dw 1010101010101010b
    b dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        xor eax, eax
        
        mov edx, 1111b
        shl edx, 28
        or eax, edx
        
        movzx edx, word [a]
        shr edx, 8
        and edx, 11b
        mov ecx, edx
        shl edx, 26
        shl ecx, 24
        or eax, edx
        or eax, ecx
        
        movzx edx, word [a]
        and edx, 1111b
        not edx
        and edx, 1111b
        shl edx, 20
        or eax, edx
        
        mov edx, eax
        shr edx, 16
        or eax, edx
        
        mov [b], eax
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
