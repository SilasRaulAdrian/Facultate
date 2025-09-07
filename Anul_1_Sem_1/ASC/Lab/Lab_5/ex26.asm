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
    s db 1, 4, 2, 3, 8, 4, 9, 5               
    ls equ 8                                 
    max_poz_pare db 0                        
    min_poz_impare db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov al, -128
        mov [max_poz_pare], al
        mov al, 127
        mov [min_poz_impare], al
        
        mov esi, s 
        mov ecx, ls
        xor edx, edx
        
    check_elements:
        mov al, [esi]
        test edx, 1
        jz check_max_pare
        
        cmp al, [min_poz_impare]
        jge skip_min_update
        mov [min_poz_impare], al
        
    skip_min_update:
        jmp next_element
        
    check_max_pare:
        cmp al, [max_poz_pare]
        jle skip_max_update
        mov [max_poz_pare], al
        
    skip_max_update:
    
    next_element:
        inc esi
        inc edx
        loop check_elements
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
