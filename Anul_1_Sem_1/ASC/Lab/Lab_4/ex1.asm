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
    a dd 00010010001101000101011001111000b
    b dd 10000111011001010100001100100001b
    c dd

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov eax, [a]
        mov ebx, [b]
        
        mov ecx, eax
        shr ecx, 11
        and ecx, 00000000000000000000000000011111b
        
        mov edx, 00000000000000000000011111100000b
        
        mov esi, ebx
        shr esi, 8
        and esi, 00000000000000000000000000001111b
        shl esi, 12
        or edx, esi
        
        shl eax, 16
        or edx, eax
        
        mov [c], edx
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
