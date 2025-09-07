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
    a dd 10101010101010101010101010101010
    b dd 11001100110011001100110011001100
    c dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov eax, [a]
        mov ebx, [b]
        xor edx, edx
        
        mov ecx, ebx
        shr ecx, 5
        and ecx, 1111b
        or edx, ecx
        
        mov ecx, eax
        and ecx, 11111b
        shl ecx, 4
        or edx, ecx
        
        mov ecx, eax
        shr ecx, 6
        and ecx, 1111111b
        shl ecx, 9
        or edx, ecx
        
        mov [c], edx
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
