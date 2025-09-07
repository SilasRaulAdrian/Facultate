bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, printf, scanf         ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
import printf msvcrt.dll
import scanf msvcrt.dll

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    prompt db 'Introduceti un numar hexazecimal (2 cifre): ', 0
    format_in db '%2s', 0
    format_unsigned db 'Numar fara semn: %u', 10, 0
    format_signed db 'Numar cu semn: %d', 10, 0
    input db 0, 0, 0 ; buffer pt 2 caractere + terminator

; our code starts here
segment code use32 class=code
    start:
        ; afisez prompt-ul
        push dword prompt
        call [printf]
        add esp, 4
        
        ; citesc input de la tastatura
        push dword input
        push dword format_in
        call [scanf]
        add esp, 4 * 2
        
        ; convertesc input-ul din ASCII in valoare numerica
        mov esi, input ; pointer catre buffer
        xor ebx, ebx ; ebx va stoca rezultatul numeric
        
        ; prima cifra
        mov al, byte [esi] ; citesc primul caracter
        call convert_hex_char ; convertesc la valoarea numerica
        shl bl, 4 ; deplasam valoarea cu 4 biti la stanga
        add bl, al ; adaugam valoarea la ebx
        
        ; a doua cifra
        inc esi
        mov al, byte [esi] ; citesc al doilea caracter
        call convert_hex_char
        add bl, al
        
        ; afisez valoarea fara semn
        push ebx
        push dword format_unsigned
        call [printf]
        add esp, 4 * 2
        
        ; afisez valoarea cu semn
        movsx eax, bl ; extind bl la 32 de biti, pastrand semnul
        push eax
        push dword format_signed
        call [printf]
        add esp, 4 * 2
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
        
    convert_hex_char:
        ; convertesc un caracter hexazecimal ASCII in valoarea sa numerica
        cmp al, '0'
        jl invalid_input
        cmp al, '9'
        jle digit
        cmp al, 'A'
        jl invalid_input
        cmp al, 'F'
        jle letter
        cmp al, 'a'
        jl invalid_input
        cmp al, 'f'
        jg invalid_input
        
    letter:
        and al, 0xDF ; transform literele mici in mari
        sub al, 'A' ; calculez offset-ul fata de 'A'
        add al, 10 ; adaug baza 10
        ret
        
    digit:
        sub al, '0' ; calculez offset-ul fata de '0'
        ret
        
    invalid_input:
        xor eax, eax
        ret