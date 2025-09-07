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
    s db 'a', 'A', 'b', 'B', '2', '%', 'x' 
    l equ 7                                
    d times l db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov esi, s 
        mov edi, d 
        mov ecx, l 
        
    filter_lowercase:
        mov al, [esi]
        
        cmp al, 'a'
        jl not_lowercase
        cmp al, 'z'
        jg not_lowercase
        
        mov [edi], al
        inc edi
        
    not_lowercase:
        inc esi
        loop filter_lowercase
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
