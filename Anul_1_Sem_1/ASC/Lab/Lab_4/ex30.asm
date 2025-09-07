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
    b dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov ax, [a]
        
        xor ax, 0x0A
        mov cx, ax
        shr cx, 1
        and cx, 0x0F
        mov dl, cl
        
        mov cx, ax
        shr cx, 7
        and cx, 0xFF
        mov bh, cl
        
        xor si, si
        
        mov di, 0x3F
        
        mov cx, ax
        shr cx, 3
        and cx, 0xFF
        not cx
        mov al, cl
        
        mov [b], dl
        mov [b + 1], bh
        mov [b + 2], si
        mov [b + 3], di
        mov [b + 4], al
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
