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
    s db 0b01011100, 0b10001001, 0b11100101 
    len_s equ ($ - s)                      

    d times 16 db 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [s]
        lea edi, [d]
        mov ecx, len_s
        
    procesare_s:
        mov al, [esi]
        xor ah, ah
        mov bl, al
        xor bh, bh
        
        xor cl, cl
        
    oglindire_biti:
        shl bl, 1
        rcr ah, 1
        inc cl
        cmp cl, 8
        jne oglindire_biti
        
        mov [edi], ah
        inc esi
        inc edi
        loop procesare_s
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
