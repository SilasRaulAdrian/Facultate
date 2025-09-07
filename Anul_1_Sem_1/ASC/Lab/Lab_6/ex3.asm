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
    sir_initial dd 0x0702090Ah, 0x0B0C0304h, 0x05060108h 
    lungime_sir equ 3                               
    sir_octeti times 12 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [sir_initial]
        lea edi, [sir_octeti]
        mov ecx, lungime_sir
        
    procesare_dublucuvant:
        lodsd
        mov ebx, eax
        
        and eax, 0x000000FF
        mov [edi], al
        inc edi
        
        mov eax, ebx
        shr eax, 8
        and eax, 0x000000FF
        mov [edi], al
        inc edi
        
        mov eax, ebx
        shr eax, 16
        and eax, 0x000000FF
        mov [edi], al
        inc edi
        
        mov eax, ebx
        shr eax, 24
        and eax, 0x000000FF
        mov [edi], al
        inc edi
        
        loop procesare_dublucuvant
        
        lea esi, [sir_octeti]
        mov ecx, 12
        
    sortare:
        mov edi, esi
        mov ebx, ecx
        dec ebx
        
    sortare_bubble:
        mov al, [edi]
        mov ah, [edi + 1]
        cmp al, ah
        jle no_swap
        
        mov [edi], ah
        mov [edi + 1], al
    
    no_swap:
        inc edi
        dec ebx
        jnz sortare_bubble
        
        dec ecx
        jnz sortare
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
