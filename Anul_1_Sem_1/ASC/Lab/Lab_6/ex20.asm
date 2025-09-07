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
    text db "cojoc capac acasa rotator refer radar repaper"  
    len_text equ $ - text                                    
    rezultate times 256 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [text]
        lea edi, [rezultate]
        xor ecx, ecx
        
    incepe_cuvant:
        push esi
        
        mov ebx, esi
        
    cauta_sfarsit:
        cmp byte [ebx], ' '
        je verifica_palindrom
        cmp byte [ebx], 0
        je verifica_palindrom
        inc ebx
        jmp cauta_sfarsit
        
    verifica_palindrom:
        dec ebx
        mov edi, ebx
        pop esi
        
    verifica:
        cmp esi, edi
        ja cuvant_palindrom
        mov al, [esi]
        mov bl, [edi]
        cmp al, bl
        jne cuvant_nepalindrom
        inc esi
        dec edi
        jmp verifica
        
    cuvant_palindrom:
        pop esi
        sub esi, text
        mov [rezultate + ecx], esi
        inc ecx
        jmp incepe_cuvant
        
    cuvant_nepalindrom:
        jmp incepe_cuvant
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
