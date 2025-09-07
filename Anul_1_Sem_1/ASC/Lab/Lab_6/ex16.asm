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
    s1 db 'aceg', 0                    
    lungime_s1 equ 4                   
    s2 db 'bdfh', 0                    
    lungime_s2 equ 4                   
    s3 times 9 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [s1]
        lea edi, [s2]
        lea ebx, [s3]
        
        mov ecx, lungime_s1
        mov edx, lungime_s2
        
    interclasare:
        cmp ecx, 0
        je copiaza_s2
        cmp edx, 0
        je copiaza_s1
        
        mov al, [esi]
        mov dl, [edi]
        cmp al, dl
        jb copiaza_s1_elem
        
        stosb
        inc esi
        dec ecx
        jmp interclasare
        
    copiaza_s1_elem:
        stosb
        inc esi
        dec ecx
        jmp interclasare
        
    copiaza_s1:
        mov al, [esi]
        stosb
        inc esi
        dec ecx
        jnz copiaza_s1
        jmp finalizare
        
    copiaza_s2:
        mov dl, [edi]
        stosb
        inc edi
        dec edx
        jnz copiaza_s2
        
    finalizare:
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
