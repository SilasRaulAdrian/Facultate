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
    s1 db '+', '4', '2', 'a', '8', '4', 'X', '5'    
    s2 db 'a', '4', '5'                             
    ls1 equ 8                                      
    ls2 equ 3                                      
    d resb ls1 + ls2

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov esi, s1
        mov edi, s2
        add edi, ls2 - 1
        mov ebx, d 
        mov ecx, 0
        
    process_s1:
        mov al, [esi]
        inc ecx
        test ecx, 3
        jnz skip_s1
        mov [ebx], al
        inc ebx
        
    skip_s1:
        inc esi
        cmp ecx, ls1
        jl process_s1
        
    process_s2:
        mov al, [edi]
        mov [ebx], al
        inc ebx
        dec edi
        dec ls2
        jnz process_s2
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
