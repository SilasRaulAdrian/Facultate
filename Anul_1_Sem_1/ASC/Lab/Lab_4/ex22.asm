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
    a dd 00000001010101010101110101100110b
    b dw 1010101111001101b
    c dw 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        xor eax, eax
        xor edx, edx
        
        mov eax, [a]
        shr eax, 20
        and eax, 11111b
        not eax
        and eax, 11111b
        
        mov edx, 1111b
        shl edx, 5
        or eax, edx
        
        mov dx, [b]
        shr dx, 12
        and dx, 1111b
        shl dx, 9
        or eax, dx
        
        mov edx, [a]
        shr edx, 7
        and edx, 111b
        shl edx, 13
        or eax, edx
        
        mov [c], ax
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
