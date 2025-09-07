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
    b db 11001100b
    c dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        xor eax, eax
        
        mov al, 1111b
        
        movzx edx, word [a]
        and edx, 1111b
        shl edx, 4
        or eax, edx
        
        movzx edx, word [a]
        shr edx, 4
        and edx, 1111111111b
        shl edx, 14
        or eax, edx
        
        movzx edx, byte [b]
        shr edx, 2
        and edx, 111111b
        shl edx, 24
        or eax, edx
        
        mov edx, 11b
        shl edx, 30
        or eax, edx
        
        mov [c], eax
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
