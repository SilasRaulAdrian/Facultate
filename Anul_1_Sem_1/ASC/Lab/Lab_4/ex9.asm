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
        xor edx, edx
        
        mov ax, [a]
        shr ax, 6
        and al, 00001111b
        mov dl, al
        
        or dl, 00110000b
        
        mov bl, [b]
        shr bl, 1
        and bl, 00000011b
        shl bl, 6
        or dl, bl
        
        mov ax, [a]
        mov cx, ax
        shl eax, 8
        or edx, eax
        mov ax, cx
        shl eax, 16
        or edx, eax
        
        mov al, [b]
        shl eax, 24
        or edx, eax
        
        mov [c], edx
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
