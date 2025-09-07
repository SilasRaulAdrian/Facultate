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
    S1 db 1, 3, 5, 7
    S2 db 2, 6, 9, 4
    len equ $-S1
    D times len * 2 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov ecx, 0 ; index pt S1
        mov edx, 0 ; index pt S2
        mov edi, 0 ; index pt D
        
    intercalare_loop:
        cmp ecx, len ; verific daca am terminat intercalarea
        je final ; daca am ajuns la sfarsitul sirului S1, atunci e gata
        
        mov al, [S1 + ecx] ; incarc elementul S1[ecx] in al
        mov [D + edi], al ; copiez in D[edi]
        inc edi ; incrementez indexul lui D
        inc ecx ; incrementez indexul lui S1
        
        mov al, [S2 + edx] ; incarc elementul S2[edx] in al
        mov [D + edi], al ; copiez in D[edi]
        inc edi ; incrementez indexul lui D
        inc edx ; incrementez indexul lui S2
        
        jmp intercalare_loop ; sar inapoi pt a continua intercalarea
        
    final: ; in acest punct D va contine sirul intercalat
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
