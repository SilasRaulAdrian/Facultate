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
    a dd 10110010111010111011100011110001b
    b dd 0
    c db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov eax, [a]
        shr eax, 14
        and eax, 00001111b
        mov cl, al
        
        mov eax, [a]
        rol eax, cl
        mov [b], eax
        
        mov eax, [b]
        shr eax, 1
        and al, 00111111b
        
        mov ebx, [b]
        shr ebx, 17
        and bl, 00000011b
        shl bl, 6
        
        or al, bl
        mov [c], al
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
