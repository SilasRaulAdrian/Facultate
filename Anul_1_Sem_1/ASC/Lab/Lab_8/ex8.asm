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
    a dd 100               
    b dd 0                 
    rezultat dd 0          
    format_in db '%d', 0   
    format_out db 'Rezultat: %d', 10, 0 
    prompt db 'Introduceti un numar natural b: ', 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        push dword prompt
        call [printf]
        add esp, 4
        
        push dword b
        push dword format_in
        call [scanf]
        add esp, 8
        
        mov eax, [a]
        xor edx, edx
        mov ebx, [b]
        
        div ebx
        mov ecx, eax
        
        mov eax, [a]
        add eax, ecx
        mov [rezultat], eax
        
        push dword [rezultat]
        push dword format_out
        call [printf]
        add esp, 8
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
