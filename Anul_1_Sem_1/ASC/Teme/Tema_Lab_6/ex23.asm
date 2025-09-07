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
    sir db 2, 4, 2, 5, 2, 2, 4, 4
    len_sir equ $ - sir
    rez dw 0
    len_rez equ 256
    count_table times 0 db 256 ; tabel de frecvente pt octeti (0-255)

; our code starts here
segment code use32 class=code
    start:
        ; ...
        xor ecx, ecx ; index pt tabelul de frecvente
        
    init_count_table:
        mov byte [count_table + ecx], 0 ; setez fiecare pozitie din tabel la 0
        inc ecx ; trec la urm pozitie
        cmp ecx, 256 ; verific daca am parcurs cele 256 pozitii
        jne init_count_table ; daca nu s-a ajuns la final, continui
        
        xor ecx, ecx ; index pt sir
    count_loop:
        movzx eax, byte [sir + ecx] ; incarc octetul curent din sir in eax
        inc byte [count_table + eax] ; cresc frecv caracterului din tabel
        inc ecx ; trec la urm octet din sir
        cmp ecx, len_sir ; verific daca am parcurs toate elementele din sir
        jne count_loop ; daca nu, continui
        
        xor ecx, ecx ; index pt tabelul de frecvente
        xor ebx, ebx ; index pt rezultatul rez
    create_result:
        movzx eax, byte [count_table + ecx] ; incarc frecv caracterului curent in eax
        cmp eax, 0 ; verific daca caracterul este inexistent
        je skip_character ; daca da, trec la urm caracter
        
        shl eax, 8 ; mut frecventa in octetul superior
        or ax, cx ; pun caracterul in octetul inferior
        mov word [rez + ebx * 2], ax ; salvez cuvantul in rez
        inc ebx ; incrementez indexul pt rez
        
    skip_character:
        inc ecx ; trec la urm caracter din tabel
        cmp ecx, 256 ; verific daca am parcurs cele 256 caractere
        jne create_result ; daca mai sunt caractere, continui
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
