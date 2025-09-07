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
    s1 db 1, 3, 6, 2, 3, 7          
    s2 db 6, 3, 8, 1, 2, 5          
    ls equ 6                        
    d times ls db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov esi, s1
        mov edi, s2
        mov ebx, d 
        mov ecx, ls
        
    process_elements:
        mov al, [esi]
        mov dl, [edi]
        cmp al, dl
        jle store_al
        mov al, dl
        
    store_al:
        mov [ebx], al
        inc esi
        inc edi
        inc ebx
        loop process_elements
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
