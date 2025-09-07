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
    m dd 00010010001101000101011001111000b
    n dd 10011010101111001101111011110000b
    p dd 0

; our code starts here
segment code use32 class=code
    start:
        ; ...
        mov eax, [m]
        mov ebx, [n]
        
        mov ecx, eax ; copiez M in ecx pt a-l folosi mai incolo
        shr ecx, 10 ; mut bitii 10-16 in pozitia 0-6
        and ecx, 01111111b ; pastrez doar 7 biti
        
        and eax, ebx ; calculez m and n
        mov edx, eax ; copiez rezultatul in edx pt a extrage urmatorii biti
        shr edx, 7 ; mut bitii 7-20 in pozitia 0-13
        and edx, 0011111111111111b ; pastrez doar 14 biti
        shl edx, 7 ; mut cei 14 biti pe pozitia 7-20
        or ecx, edx ; combin cu rezultatul anterior
        
        mov edx, ebx ; copiez n in edx pt a extrage bitii necesari
        shr edx, 1 ; mut bitii 1-11 in pozitia 0-10
        and edx, 0000011111111111b ; pastrez doar 11 biti
        shl edx, 21 ; mut cei 11 biti pe pozitia 21-31
        or ecx, edx ; combin cu rezultatul anterior
        
        mov [p], ecx ; stochez in p rezultatul
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
