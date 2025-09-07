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
    c db 0
    d dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        ; calculam octetul c
        mov ax, [a]
        shr ax, 5
        and al, 00111111b
        
        mov bx, [b]
        shr bx, 1
        and bl, 00000011b
        shl bl, 6
        
        or al, bl
        mov [c], al
        
        ; calculam dublucuvantul d
        xor edx, edx
        
        mov bx, [b]
        shr bx, 8
        mov dl, bl
        
        mov al, [c]
        mov dh, al
        
        mov ax, [a]
        shr ax, 8
        mov cl, al
        mov ch, al
        shl eax, 16
        or edx, eax
        
        mov ax, [a]
        and al, 11111111b
        shl eax, 24
        or edx, eax
        
        mov [d], edx
        
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
