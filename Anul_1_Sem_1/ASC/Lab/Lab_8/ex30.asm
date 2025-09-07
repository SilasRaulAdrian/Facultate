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
    current_number dd 0              
    min_number dd 0x7FFFFFFF         
    format_in db '%d', 0              
    format_out db 'Cel mai mic numar este: %d', 10, 0 
    prompt_input db 'Introduceti un numar (0 pentru a termina): ', 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
    loop_input:
        push dword prompt_input
        call [printf]
        add esp, 4
        
        push dword current_number
        push dword format_in
        call [scanf]
        add esp, 8
        
        mov eax, [current_number]
        cmp eax, 0
        je print_min
        
        mov ebx, [min_number]
        cmp eax, ebx
        jge loop_input
        
        mov [min_number], eax
        jmp loop_input
        
    print_min:
        push dword [min_number]
        push dword format_out
        call [printf]
        add esp, 8
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
