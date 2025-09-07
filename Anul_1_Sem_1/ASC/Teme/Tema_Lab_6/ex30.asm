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
    cuvinte dw 1234h, 5678h, 9ABCh, 0DE10h
    len_cuvinte equ 4
    s1 db 0, 0, 0, 0
    s2 db 0, 0, 0, 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        xor ecx, ecx ; indexul pt cuvinte
    parcurge_cuvinte:
        mov ax, [cuvinte + ecx * 2] ; incarc cuvantul curent in ax
        
        mov al, byte [cuvinte + ecx * 2] ; extrag octetul low in al
        mov ah, byte [cuvinte + ecx * 2 + 1] ; extrag octetul high in ah
        
        mov bl, al ; salvez octetul low in bl pt numarare
        xor edx, edx ; nr de biti 1 pt octetul low
    numara_biti_low:
        test bl, 1 ; verific ultimul bit din bl
        jz nu_incrementeaza_low ; daca bitul nu este 1, sar
        inc edx ; incrementez nr de biti 1
    nu_incrementeaza_low:
        shr bl, 1 ; verific urm bit
        jnz numara_biti_low ; daca mai sunt biti de verificat, continui
        
        mov bl, ah ; salvez octetul high in bl pt numarare
        xor ecx, ecx ; nr de biti 1 pt octetul high
    numara_biti_high:
        test bl, 1
        jz nu_incrementeaza_high
        inc ecx
    nu_incrementeaza_high:
        shr bl, 1
        jnz numara_biti_high
        
        ; daca nr de biti 1 din high > low, s1 va contine octetul high si s2 va contine octetul low
        mov [s1 + ecx], ah ; stochez octetul high in s1
        mov [s2 + ecx], al ; stochez octetul low in s2
        jmp continuare
        
    biti_egali:
        ; daca nr de biti 1 din high = low
        mov [s1 + ecx], dl ; stochez nr de biti 1 in s1
        mov byte [s2 + ecx], 0 ; setez s2 la 0
        jmp continuare
        
    low_mai_mare:
        ; daca nr de biti 1 din high < low
        mov [s1 + ecx], al
        mov [s2 + ecx], ah
        
    continuare:
        inc ecx ; trec la urm cuvant
        cmp ecx, len_cuvinte ; verific daca am parcurs toate cuvintele
        jl parcurge_cuvinte ; daca nu, continui
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
