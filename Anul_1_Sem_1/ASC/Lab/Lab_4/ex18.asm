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
        
        movzx edx, word [a]
        shr edx, 8
        and edx, 1111b
        shl edx, 4
        or eax, edx
        
        movzx edx, word [a]
        and edx, 11b
        not edx
        and edx, 11b
        mov ecx, edx
        shl edx, 8
        shl ecx, 10
        or eax, edx
        or eax, ecx
        
        mov edx, 1111b
        shl edx, 12
        or eax, edx
        
        shl eax, 16
        shr eax, 16
        or eax, eax
        
        mov [b], eax
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
