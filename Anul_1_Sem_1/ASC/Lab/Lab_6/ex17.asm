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
    sir dd 0x12345678, 0x1256ABCD, 0x12AB4344
    lungime equ 3

; our code starts here
segment code use32 class=code
    start:
        ; ...
        lea esi, [sir]
        mov ecx, lungime
        shl ecx, 1
        lea edi, [sir]
        
    sortare:
        mov ebx, ecx
        dec ebx
        jz sfarsit_sortare
        
        lea esi, [sir]
        
    sortare_bubble:
        mov ax, [esi]
        mov dx, [esi + 4]
        cmp ax, dx
        ja schimba
        
    continua:
        add esi, 4
        dec ebx
        jnz sortare_bubble
        jmp sortare
        
    schimba:
        mov [esi], dx
        mov [esi + 4], ax
        jmp continua
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
