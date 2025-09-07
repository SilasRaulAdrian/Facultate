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
    S db 1, 2, 3, 4, 5, 6
    len equ $-S
    D times len - 1 db 0
    
; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov ecx, 0 ; indexul pt sirul D
        mov esi, 0 ; indexul pt sirul S
        
    suma_loop:
        ; verific daca am ajuns la sfarsitul sirului S
        cmp esi, len - 1 ; conditia se opreste cand am ajuns la penultimul element din S
        jae final ; daca conditia e indeplinita, atunci e gata
        
        mov al, [S + esi] ; incarc elementul S[esi] in al
        mov bl, [S + esi + 1] ; incarc elementul S[esi+1] in al
        add al, bl ; adun cei 2 octeti si stochez rezultatul in al
        mov [D + ecx], al ; stochez rezultatul in sirul D la pozitia curenta (ecx)
        inc esi ; incrementez indexul lui S
        inc ecx ; incrementez indexul lui D
        
        jmp suma_loop ; sar inapoi pt a continua sumele
        
    final: ; in acest punct sirul D contine elementele cerute
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
