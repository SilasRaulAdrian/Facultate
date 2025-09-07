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
    m dd 0                  
    n dd 0                 
    result dd 0             
    format_in db '%d', 0    
    format_out db 'Numarul intreg reprezentat de bitii izolati: %d', 10, 0 
    prompt_a db 'Introduceti un numar a (baza 10): ', 0
    prompt_m db 'Introduceti valoarea m: ', 0
    prompt_n db 'Introduceti valoarea n: ', 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        push dword prompt_a
        call [printf]
        add esp, 4
        
        push dword a
        push dword format_in
        call [scanf]
        add esp, 8
        
        push dword prompt_m
        call [printf]
        add esp, 4
        
        push dword m
        push dword format_in
        call [scanf]
        add esp, 8
        
        push dword prompt_n
        call [printf]
        add esp, 4
        
        push dword n
        push dword format_in
        call [scanf]
        add esp, 8
        
        mov ax, [m]
        sub ax, [n]
        add ax, 1
        mov cx, ax
        dec cx
        mov ax, 1
        shl ax, cl
        sub ax, 1
        mov bx, ax
        mov ax, [a]
        shr ax, cl 
        and ax, bx
        mov [result], ax
        
        push dword [result]
        push dword format_out
        call [printf]
        add esp, 8
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
