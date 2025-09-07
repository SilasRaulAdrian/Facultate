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
    s dw 1234, 5678, 3456, 7890, 1230, 5679 
    len_s equ ($ - s) / 2                  

    d times 16 dw 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [s]
        lea edi, [d]
        mov ecx, len_s
        
        mov ebx, 0
        mov edx, 0
        mov edi, esi
        mov esi, [s]
        
    procesare_s:
        cmp ecx, 1
        jle finalizare
        
        mov ax, [esi]
        mov bx, [esi + 2]
        
        cmp ax, bx
        jge reset_subsir
        
        inc edx
        jmp next_element
        
    reset_subsir:
        cmp edx, ebx
        jle reset_length
        
        mov ebx, edx
        lea edi, [esi - edx * 2]
        
    reset_length:
        mov edx, 1
        
    next_element:
        add esi, 2
        loop procesare_s
        
    finalizare:
        mov ecx, ebx
        lea esi, [edi]
        lea edi, [d]
        
    copiere_subsir:
        mov ax, [esi]
        mov [edi], ax
        add esi, 2
        add edi, 2
        loop copiere_subsir
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
