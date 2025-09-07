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
    s1 db 1, 2, 3, 4           
    s2 db 5, 6, 7, 8           
    l equ 4                    
    d times l dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov esi, s1
        mov edi, s2
        mov ebx, d
        mov ecx, l 
        xor edx, edx
        
    process_elements:
        movzx eax, byte [esi]
        movzx ebx, byte [edi]
        
        test dl, 1
        jz even_position
        
        sub eax, ebx
        jmp store_result
        
    even_position:
        add eax, ebx
        
    store_result:
        mov [d + edx * 4], eax
        inc esi
        inc edi
        inc edx
        loop process_elements
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
