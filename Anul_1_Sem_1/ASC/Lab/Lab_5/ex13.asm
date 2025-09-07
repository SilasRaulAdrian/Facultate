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
    s db 1, 5, 3, 8, 2, 9       
    l equ 6                     
    d1 times (l / 2) db 0       
    d2 times (l / 2) db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov esi, s 
        mov edi, d1
        mov ebx, d2
        mov ecx, l
        
    partition_elements:
        test byte [esi - s], 1
        jz save_to_d1
        
        mov al, [esi]
        mov [ebx], al
        inc ebx
        jmp next_element
        
    save_to_d1:
        mov al, [esi]
        mov [edi], al
        inc edi
        
    next_element:
        inc esi
        loop partition_elements
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
