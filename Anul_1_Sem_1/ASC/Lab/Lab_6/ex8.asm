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
    sir_initial dd 12345678h, 1A2C3C4Dh, 98FCDC76h
    lungime_sir equ 3                              
    sir_rezultat times 3 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [sir_initial]
        lea edi, [sir_rezultat]
        mov ecx, lungime_sir
        
    procesare_sir:
        lodsd
        
        shr eax, 16
        mov bx, ax
        
        push ebx
        xor edx, edx
        mov ecx, 10
        mov esi, 0
        
    palindrom_loop:
        xor edx, edx
        div ecx
        add esi, edx
        mul ecx
        add esi, eax
        cmp bx, 0
        jne palindrom_loop
        
        pop ebx
        cmp ebx, esi
        jne next_element
        
        mov al, bl
        mov [edi], al
        inc edi
        
    next_element:
        loop procesare_sir
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
