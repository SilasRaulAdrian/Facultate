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
    a dd 11010010111000101110000001000000b, 00000000000000000000000000000000b
    b resd 1
    c resb 1

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov eax, [a]
        shr eax, 35
        and eax, 0000000000000111b
        mov ebx, eax
        
        mov eax, [a]
        ror eax, bl
        
        mov [b], eax
        
        mov al, byte [b]
        shr al, 8
        and al, 00001111b
        mov byte [c], al
        
        mov al, byte [b + 1]
        shr al, 4
        and al, 00001111b
        or byte [c], al << 4
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
