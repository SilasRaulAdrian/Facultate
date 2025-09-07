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
    sir1 db 1, 2, 3, 4, 2, 3, 4, 5, 6  
    len1 equ 9                         
    sir2 db 2, 3, 4                  
    len2 equ 3                        
    rezultate times 9 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [sir1]
        lea edi, [sir2]
        lea ebx, [rezultate]
        xor ecx, ecx
        mov edx, len1
        sub edx, len2
        
    cauta:
        mov edi, sir2
        push esi
        mov ebx, len2
        
    compara:
        mov al, [esi]
        mov al, [edi]
        jne urmator
        inc esi
        inc edi
        dec ebx
        jnz compara
        
        pop esi
        sub esi, [sir1]
        mov [rezultate + ecx], esi
        inc ecx
        jmp final
        
    urmator:
        pop esi
        inc esi
        cmp esi, sir1 + edx
        jbe cauta
        
    final:
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
