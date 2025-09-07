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
    a dw 1111000011110000b
    b dw 1010101010101010b
    c dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        xor eax, eax
        mov eax, 11111111111111110000000000000000b
        or eax, 00000000000000000000000000111000b
        
        movzx edx, word [a]
        shr edx, 11
        and edx, 1111b
        shl edx, 6
        or eax, edx
        
        movzx edx, word [b]
        shr edx, 1
        and edx, 111111b
        shl edx, 10
        or eax, edx
        
        mov [c], eax
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
