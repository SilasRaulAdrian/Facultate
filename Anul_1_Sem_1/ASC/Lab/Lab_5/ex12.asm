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
    s1 db 'a', 'b', 'c', 'd', 'e', 'f'        
    l1 equ 6                                
    s2 db '1', '2', '3', '4', '5'            
    l2 equ 5                                
    d times (l1 / 2 + l2 / 2) db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov esi, s2
        mov edi, d 
        mov ecx, l2
        
    copy_s2_even:
        test byte [esi - s2], 1
        jnz skip_s2_even
        
        mov al, [esi]
        mov [edi], al
        inc edi
        
    skip_s2_even:
        inc esi
        loop copy_s2_even
        
        mov esi, s1
        mov ecx, l1
        
    copy_s1_odd:
        test byte [esi - s1], 1
        jz skip_s1_odd
        
        mov al, [esi]
        mov [edi], al
        inc edi
        
    skip_s1_odd:
        inc esi
        loop copy_s1_odd
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
