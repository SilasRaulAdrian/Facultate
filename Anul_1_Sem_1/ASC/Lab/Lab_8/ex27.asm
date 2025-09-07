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
    sir db 'Acesta este un exemplu de text.', 0
    caracter db 0                              
    numar_aparitii dd 0                        
    format_in db '%c', 0                       
    format_out db 'Caracterul "%c" apare de %d ori.', 10, 0 
    prompt_caracter db 'Introduceti caracterul: ', 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        push dword prompt_caracter
        call [printf]
        add esp, 4
        
        push dword caracter
        push dword format_in
        call [scanf]
        add esp, 8
        
        xor ecx, ecx
        mov esi, sir
        
    contor:
        mov al, [esi]
        cmp al, 0
        je afisare
        cmp al, [caracter]
        jne next_char
        inc ecx
        
    next_char:
        inc esi
        jmp contor
        
    afisare:
        mov [numar_aparitii], ecx
        
        push dword [numar_aparitii]
        push dword [caracter]
        push dword format_out
        call [printf]
        add esp, 12
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
