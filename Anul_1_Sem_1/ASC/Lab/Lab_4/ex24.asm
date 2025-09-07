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
    m dd 0x12345678
    mnew dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov eax, [m]
        shr eax, 5
        and eax, 00001111b
        mov ebx, eax
        
        mov eax, 11110000b
        or ebx, eax
        
        mov eax, [m]
        shr eax, 8
        and eax, 00000000FFFFFFFFFFFFF
        shl eax, 8
        or ebx, eax
        
        mov [mnew], ebx
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
