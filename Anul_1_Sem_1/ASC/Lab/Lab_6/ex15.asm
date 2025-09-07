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
    sir_initial dd 12345607h, 1A2B3C15h                    
    lungime_sir equ 2                                      
    sir_d times 8 db 0 

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [sir_initial]
        lea edi, [sir_d]
        mov ecx, lungime_sir
        
    procesare:
        lodsd
        mov ebx, eax
        
        and ebx, 0x000000FF
        stosb
        
        mov ebx, eax
        shr ebx, 8
        and ebx, 0x000000FF
        stosb
        
        mov ebx, eax
        shr ebx, 16
        and ebx, 0x000000FF
        stosb
        
        mov ebx, eax
        shr ebx, 24
        and ebx, 0x000000FF
        stosb
        
        loop procesare
        
        lea esi, [sir_d]
        mov ecx, 8
        
    sortare_desc:
        mov edi, esi
        mov ebx, ecx
        
    inner_loop_desc:
        mov al, [edi]
        mov bl, [edi + 1]
        cmp al, bl
        jae no_swap_desc
        
        mov [edi], bl
        mov [edi + 1], al
        
    no_swap_desc:
        inc edi
        dec ebx
        cmp ebx, 1
        ja inner_loop_desc
        
        dec ecx
        cmp ecx, 1
        ja sortare_desc
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
