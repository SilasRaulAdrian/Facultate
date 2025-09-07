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
    sir_initial dd 12345678h, 9ABCDEF0h, 56781234h, 0ABCD5678h 
    lungime_sir equ 4                                       
    sir_b1 times 4 db 0                                     
    sir_b2 times 4 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [sir_initial]
        lea edi, [sir_b1]
        mov ecx, lungime_sir
        
    procesare_b1:
        lodsd
        shr eax, 16
        mov al, ah
        stosb
        loop procesare_b1
        
        lea esi, [sir_initial]
        lea edi, [sir_b2]
        mov ecx, lungime_sir
        
    procesare_b2:
        lodsd
        and eax, 0xFFFF
        mov al, al
        stosb
        loop procesare_b2
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
