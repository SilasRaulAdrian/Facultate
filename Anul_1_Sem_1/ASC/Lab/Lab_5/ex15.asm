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
    a db 2, 1, 3, 3, 4, 2, 6       
    la equ 7                      
    b db 4, 5, 7, 6, 2, 1         
    lb equ 6                      
    r times (la + lb) db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov esi, b
        add esi, lb - 1
        mov edi, r 
        mov ecx, lb
        
    copy_b_reverse:
        mov al, [esi]
        mov [edi], al
        dec esi
        inc edi
        loop copy_b_reverse
        
        mov esi, a 
        mov ecx, la 
        
    copy_a_odd:
        mov al, [esi]
        test al, 1
        jz skip_a_odd
        
        mov [edi], al
        inc edi
        
    skip_a_odd:
        inc esi
        loop copy_a_odd
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
