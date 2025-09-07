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
    c dq 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov ax, [a]
        mov bx, [b]
        
        mov cx, ax
        shr cx, 21
        and cx, 0xFF
        mov dl, cl
        
        mov cx, bx
        shr cx, 23
        and cx, 0xFF
        not cx
        mov dh, cl
        
        mov al, 101010b
        
        xor si, si
        
        mov cx, bx
        shr cx, 21
        and cx, 0x7FF
        mov di, cx
        
        mov cx, ax
        shr cx, 1
        and cx, 0x1FFF
        mov si, cx
        
        mov ax, [a]
        xor ax, 0x0AB
        shr ax, 24
        and ax, 0xFF
        mov bx, ax
        
        mov [c], dl
        mov [c + 1], dh
        mov [c + 2], al
        mov [c + 3], si
        mov [c + 4], di
        mov [c + 6], si
        mov [c + 8], bx
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
