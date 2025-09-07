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
    a db 10101010b
    b db 11001100b
    c dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov al, [a]
        mov bl, [b]
        xor edx, edx
        
        mov eax, 1111111111111111b
        shl eax, 16
        or edx, eax
        
        mov ecx, ebx
        shr ecx, 3
        and ecx, 1111b
        or edx, ecx
        
        mov ecx, 110b
        shl ecx, 8
        or edx, ecx
        
        mov ecx, eax
        and ecx, 11111b
        shl ecx, 11
        or edx, ecx
        
        mov [c], edx
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
