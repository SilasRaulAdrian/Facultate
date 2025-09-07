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
    r dd 10001001101010111100110111101111b
    t dd 00010010001101000101011001111000b
    q dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov eax, [t]
        mov ebx, [r]
        
        mov ecx, eax ; copiez T in ecx pt a-l folosi mai incolo
        shr ecx, 10 ; mut bitii 10-16 in pozitia 0-6
        and ecx, 01111111b ; pastrez doar 7 biti
        
        xor eax, ebx ; calculez r xor t
        mov edx, eax ; copiez rezultatul in edx pt a extrage bitii necesari
        shr edx, 7 ; mut bitii 7-24 in pozitia 0-17
        and edx, 1111111111111111b ; pastrez doar 18 biti
        shl edx, 7 ; mut cei 18 biti in pozitia 7-24
        or ecx, edx ; combin cu rezultatul anterior
        
        mov edx, ebx ; copiez r in edx pt a extrage bitii necesari
        shr edx, 5 ; mut bitii 5-11 in pozitia 0-6
        and edx, 01111111b ; pastrez doar 7 biti
        shl edx, 25 ; mut cei 7 biti in pozitia 25-31
        or ecx, edx ; combin cu rezultatul anterior
        
        mov [q], ecx ; stochez in q rezultatul
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
