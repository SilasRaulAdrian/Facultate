bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    a db 2
    b dw 15
    c dd 20
    d dq 80

; our code starts here
segment code use32 class=code
    start:
        ; Interpretare fara semn
        
        ; ex 26: (c-b+a)-(d+a)
        ; mov eax, 0
        ; mov al, [a]
        
        ; mov ebx, 0
        ; mov bx, [b]
        ; mov ecx, [c]
        
        ; sub ecx, ebx
        ; add ecx, eax
        
        ; mov edx, [d]
        ; add edx, eax
        
        ; sub ecx, edx
        
        ; ex 27: (a+c)-(d+b)
        ; mov eax, 0
        ; mov al, [a]
        ; mov ecx, 0
        ; mov cx, [c]
        
        ; add eax, ecx
        
        ; mov edx, [d]
        ; mov ebx, 0
        ; mov bx, [b]
        
        ; add edx, ebx
        ; sub eax, edx
        
        ; ex 28: d-(a+b)+(c+c)
        ; mov edx, [d]
        ; mov eax, 0
        ; mov al, [a]
        ; mov ebx, 0
        ; mov bx, [b]
        
        ; add eax, ebx
        
        ; mov ecx, 0
        ; mov cx, [c]
        ; add ecx, ecx
        
        ; sub edx, eax
        ; add edx, ecx
        
        ; Interpretare cu semn
        ; ex 21: d-a+(b+a-c)
        ; mov eax, 0
        ; mov al, [a]
        ; cbw
        ; cwde
        
        ; mov ebx, 0
        ; mov bx, [b]
        ; cwde
        
        ; mov ecx, [c]
        ; mov edx, [d]
        
        ; add ebx, eax
        ; sub ebx, ecx
        
        ; sub edx, eax
        ; add edx, ebx
        
        ; ex 22: c+b-(a-d+b)
        ; mov eax, 0
        ; mov al, [a]
        ; cbw
        ; cwde
        
        ; mov ebx, 0
        ; mov bx, [b]
        ; cwde
        
        ; mov ecx, [c]
        ; mov edx, [d]
        
        ; add ecx, ebx
        ; sub eax, edx
        ; add eax, ebx
        ; sub ecx, eax
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
