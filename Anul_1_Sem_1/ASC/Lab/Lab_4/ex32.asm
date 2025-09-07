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
    a dw 0x1234
    b dw 0xABCD
    c dw 0x5678
    d db 0
    e db 0
    f db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov ax, [a]
        mov bx, [b]
        mov cx, [c]
        
        and ax, 0x1F
        
        shr bx, 5
        and bx, 0x1F
        
        or ax, bx
        mov [d], al
        
        shr cx, 10
        and cx, 0x1F
        
        mov [e], cl
        
        sub al, [e]
        mov [f], al
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
