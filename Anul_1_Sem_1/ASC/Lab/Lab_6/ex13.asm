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
    sir_initial dd 12345607h, 1A2B3C15h, 13A33412h         
    lungime_sir equ 3                                     
    sir_d times 4 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [sir_initial]
        lea edi, [sir_d]
        mov ecx, lungime_sir
        
    procesare:
        lodsd
        and eax, 0xFFFF
        and ax, 0x00FF
        mov bl, al
        test bl, bl
        jz next_element
        movzx ebx, bl
        mov edx, 0
        mov eax, ebx
        mov ebx, 7
        div ebx
        cmp edx, 0
        jne next_element
        stosb
        
    next_element
        loop procesare
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
