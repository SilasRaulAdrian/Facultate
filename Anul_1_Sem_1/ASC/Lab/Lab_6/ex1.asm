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
    sir_initial dd 0x127F5678, 0x0ABCDABCD 
    lungime_sir equ 2                       
    sir_rezultat times 2 dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [sir_initial]
        lea edi, [sir_rezultat]
        mov ecx, lungime_sir
        
    procesare_dublucuvant:
        lodsd
        movsx ebx, al
        movsx edx, ah
        shr eax, 16
        movsx esi, al
        movsx edi, ah
        add ebx, esi
        add edx, edi
        
        shl eax, 16
        mov ax, bx
        or eax, edx
        stosd
        loop procesare_dublucuvant
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
