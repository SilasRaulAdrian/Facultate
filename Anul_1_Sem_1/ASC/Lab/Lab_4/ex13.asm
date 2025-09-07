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
    octet1 db 10101100b
    octet2 db 11011001b
    octet3 db 01110110b
    octet4 db 11100010b
    result dw 0          

; our code starts here
segment code use32 class=code
    start:
        ; ...
        xor ax, ax
        
        mov al, [octet1]
        shr al, 4
        and al, 00000111b
        add ax, ax
        
        mov al, [octet2]
        shr al, 4
        and al, 00000111b
        add ax, ax
        
        mov al, [octet3]
        shr al, 4
        and al, 00000111b
        add ax, ax
        
        mov al, [octet4]
        shr al, 4
        and al, 00000111b
        add ax, ax
        
        mov [result], ax
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
