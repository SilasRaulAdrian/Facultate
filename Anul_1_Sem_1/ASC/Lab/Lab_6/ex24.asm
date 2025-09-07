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
    input dd 0x12345678, 0xAABBCCDD, 0xFFFFFFFF, 0x87654321
    len_input equ ($ - input) / 4                          

    output times 16 dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [input]
        lea edi, [output]
        mov ecx, len_input
        
    procesare_input:
        mov eax, [esi]
        
        xor ebx, ebx
        mov edx, eax
        
    contor_biti:
        test edx, 1
        jz next_bit
        inc ebx
        
    next_bit:
        shr edx, 1
        jnz contor_biti
        
        test ebx, 1
        jnz skip_dword
        
        mov [edi], eax
        add edi, 4
        
    skip_dword:
        add esi, 4
        loop procesare_input
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
