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
    l1 equ 4                 
    s2 db 5, 6, 7            
    l2 equ 3                  
    d times (l1 + l2) db 0    

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov esi, s1
        mov edi, d
        mov ecx, l1
    
    copy_s1:
        mov al, [esi]
        mov [edi], al
        inc esi
        inc edi
        loop copy_s1
        
        mov esi, s2
        add esi, l2 - 1
        mov ecx, l2
        
    copy_s2_reverse:
        mov al, [esi]
        mov [edi], al
        dec esi
        inc edi
        loop copy_s2_reverse
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
