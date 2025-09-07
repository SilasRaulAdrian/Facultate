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
    a db 2, 1, 3, -3, -4, 2, -6    
    b db 4, 5, -5, 7, -6, -2, 1    
    la equ 7                      
    lb equ 7                      
    r times (la + lb) db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov esi, a 
        mov edi, r 
        mov ecx, la 
        
    process_a:
        mov al, [esi]
        cmp al, 0
        jge skip_a
        test al, 1
        jnz skip_a
        
        mov [edi], al
        inc edi
        
    skip_a:
        inc esi
        loop process_a
        
        mov esi, b
        mov ecx, lb
        
    process_b:
        mov al, [esi]
        cmp al, 0
        jge skip_b
        test al, 1
        jnz skip_b
        
        mov [edi], al
        inc edi
        
    skip_b:
        inc esi
        loop process_b
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
