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
    a dd 00000000000000000011001100000000b
    a_high dd 00000000000000000011001100000000b
    a_low dd 00000000000000000011001100000000b
    b dd 0x00000000
    c db 0x00

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov eax, [a_high]
        shr eax, 17
        and eax, 00000111b
        mov ebx, eax
        
        mov eax, [a_high]
        rol eax, bl
        mov [b], eax
        
        movzx eax, byte [b]
        shr eax, 9
        and eax, 00000111b
        mov [c], al
        
        movzx eax, byte [b]
        shr eax, 20
        and eax, 00011111b
        or byte [c], al
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
