bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, printf, scanf               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
import printf msvcrt.dll
import scanf msvcrt.dll

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    a dd 23             
    b dd 5             
    _rest dd 0           
    format_out db '%d mod %d = %d', 10, 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov eax, [a]
        xor edx, edx
        mov ebx, [b]
        
        div ebx
        mov [_rest], edx
        
        push dword [_rest]
        push dword [b]
        push dword [a]
        push dword format_out
        call [printf]
        add esp, 16
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
