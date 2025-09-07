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
    sir_initial db 5, 25, 55, 127          
    lungime_sir equ 4                    
    sir_rezultat times 4 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [sir_initial]
        lea edi, [sir_rezultat]
        mov ecx, lungime_sir
        
    procesare_octeti:
        lodsb
        
        mov bl, al
        xor bh, bh
        mov dl, 8
        
    contor_biti:
        test bl, 1
        jz skip_incr
        inc bh
    skip_incr:
        shr bl, 1
        dec dl
        jnz contor_biti
        
        mov [edi], bh
        inc edi
        loop procesare_octeti
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
