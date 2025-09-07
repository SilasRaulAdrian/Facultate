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
    d times ls1 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov esi, s1
        mov edi, d 
        mov ecx, ls1
        
    next_element_s1:
        mov al, [esi]
        
        mov ebx, s2
        mov edx, ls2
        
    check_in_s2:
        cmp edx, 0
        je add_to_d
        mov bl, [ebx]
        cmp al, bl
        je skip_add
        inc ebx
        dec edx
        jmp check_in_s2
        
    add_to_d:
        mov [edi], al
        inc edi
        
    skip_add:
        inc esi
        loop next_element_s1
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
