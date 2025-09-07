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
    sir_initial dw 0x1432, 0x8675, 0x0ADBC 
    lungime_sir equ 3                      
    sir_rezultat times 3 dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [sir_initial]
        lea edi, [sir_rezultat]
        mov ecx, lungime_sir
        
    procesare_cuvant:
        lodsw
        movzx ebx, al
        and ebx, 0x0F
        
        movzx edx, al
        shr edx, 4
        
        movzx esi, ah
        and esi, 0x0F
        
        movzx edi, ah
        shr edi, 4
        
        mov eax, ebx
        cmp eax, edx
        jbe skip_swap1
        xchg eax, edx
        
    skip_swap1:
        cmp eax, esi
        jbe skip_swap2
        xchg eax, esi
    skip_swap2:
        cmp eax, edi
        jbe skip_swap3
        xchg eax, edi
    skip_swap3:
        mov ebx, edx
        cmp ebx, esi
        jbe skip_swap4
        xchg ebx, esi
    skip_swap4:
        cmp ebx, edi
        jbe skip_swap5
        xchg ebx, edi
    skip_swap5:
        cmp esi, edi
        jbe skip_swap6
        xchg esi, edi
    skip_swap6:
        
        mov eax, 0
        shl edi, 4
        or eax, edi
        
        shl esi, 4
        or eax, esi
        
        shl ebx, 4
        or eax, ebx
        
        shl eax, 4
        or eax, eax
        
        stosd
        loop procesare_cuvant
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
