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
    b dw 1100110011001100b
    c dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        xor edx, edx
        mov dl, 00011111b
        
        mov ax, [a]
        and ax, 01111111b
        shl ax, 5
        or dx, ax
        
        mov ax, 0000000001100101b
        shl eax, 16
        or edx, eax
        
        mov ax, [b]
        shr ax, 8
        and ax, 00001111b
        shl ax, 12
        or dx, ax
        
        mov [c], edx
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
