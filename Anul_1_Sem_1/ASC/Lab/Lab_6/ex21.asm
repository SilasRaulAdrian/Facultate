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
    sir dw 12345, 20778, 4596         
    len_sir equ ($ - sir) / 2          
    rezultate times 64 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [sir]
        lea esi, [rezultate]
        mov ecx, len_sir
        
    procesare_cuvinte:
        mov ax, [esi]
        
        xor ebx, ebx
        mov bx, ax
        
    extrage_loop:
        xor edx, edx
        mov eax, ebx
        mov ecx, 10
        div ecx
        add dl, '0'
        inc edi
        mov ebx, eax
        test ebx, ebx
        jnz extrage_loop
        
        add esi, 2
        loop procesare_cuvinte
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
