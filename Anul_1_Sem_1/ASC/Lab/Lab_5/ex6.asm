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
    s db 1, 2, 3, 4, 5, 6, 7, 8 
    l equ 8                    
    d times l db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov esi, s 
        mov edi, d 
        mov ecx, l 
        xor edx, edx
        
    copy_even:
        mov al, [esi + edx]
        test edx, 1
        jnz skip_even
        
        mov [edi], al
        inc edi
        
    skip_even:
        inc edx
        loop copy_even
        
        mov esi, s 
        mov ecx, l 
        xor edx, edx
        
    copy_odd:
        mov al, [esi + edx]
        test edx, 1
        jz skip_odd
        
        mov [edi], al
        inc edi
        
    skip_odd:
        inc edx
        loop copy_odd
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
