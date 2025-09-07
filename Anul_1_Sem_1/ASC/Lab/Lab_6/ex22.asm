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
    input db 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 
    len_input equ ($ - input)              

    src db 2, 4, 6                       
    dst db 20h, 40h, 60h                  
    len_src equ ($ - src)                 

    output times 64 db 0 

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [input]
        lea edi, [output]
        mov ecx, len_input
        
    procesare_input:
        mov al, [esi]
        lea ebx, [src]
        mov edx, len_src
        
        xor ah, ah
        
    cautare_in_src:
        cmp al, [ebx]
        je inlocuire
        
        inc ebx
        dec edx
        jnz cautare_in_src
        
        mov [edi], al
        jmp urmatorul_octet
        
    inlocuire:
        lea ebx, [dst]
        sub ebx, len_src
        neg edx
        add ebx, edx
        mov al, [ebx]
        mov [edi], al
        
    urmatorul_octet:
        inc esi
        inc edi
        loop procesare_input
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
