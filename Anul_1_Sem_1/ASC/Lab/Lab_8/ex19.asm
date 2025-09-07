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
    a dd 0                 
    b dd 0                 
    _mask dw 0               
    format_in_byte db '%x', 0 
    format_in_word db '%x', 0
    format_yes db 'DA (bitii se regasesc)', 10, 0
    format_no db 'NU (bitii NU se regasesc)', 10, 0
    prompt_a db 'Introduceti octetul (baza 16): ', 0
    prompt_b db 'Introduceti cuvantul (baza 16): ', 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        push dword prompt_a
        call [printf]
        add esp, 4
        
        push dword a
        push dword format_in_byte
        call [scanf]
        add esp, 8
        
        push dword prompt_b
        call [printf]
        add esp, 4
        
        push dword b
        push dword format_in_word
        call [scanf]
        add esp, 8
        
        mov eax, [a]
        mov ebx, [b]
        mov ecx, 8
        
    check_bits:
        mov edx, ebx
        and edx, 0xFF
        cmp edx, eax
        je found
        shr ebx, 1
        loop check_bits
        
    not_found:
        push dword format_no
        call [printf]
        add esp, 4
        jmp end_program
        
    found:
        push dword format_yes
        call [printf]
        add esp, 4
        jmp end_program
        
    end_program:
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
