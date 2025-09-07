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
    s db '+', '4', '2', 'a', '8', '4', 'X', '5'    
    ls equ $ - s                                 
    d resb ls

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov esi, s 
        mov edi, d 
        mov ecx, ls
        
    process_elements:
        mov al, [esi]
        cmp al, '0'
        jl not_digit
        cmp al, '9'
        jg not_digit
        mov [edi], al
        inc edi
        
    not_digit:
        inc esi
        loop process_elements
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
