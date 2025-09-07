bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ex 21
    ; a db 3
    ; b dw 2
    ; e dd 100
    ; x dq 20
    
    ; ex 22
    ; a db 5
    ; b db 4
    ; c db 3
    ; e dd 10
    ; x dq 20
    
    ; ex 23
    ; a db 5
    ; b db 4
    ; c db 3
    ; d db 2
    ; e dw 1
    ; x dq 10
    
    ; ex 24
    ; a dd 10
    ; b db 4
    ; c db 8
    ; d db 2
    ; x dq 20
    
    ; ex 30
    ; a dw 10
    ; b db 4
    ; c db 8
    ; e dd 20
    ; x dq 30

; our code starts here
segment code use32 class=code
    start:
        ; ex 21: (a*a/b+b*b)/(2+b)+e-x
        ; mov eax, 0
        ; mov al, [a]
        
        ; mov ecx, eax
        ; mul ecx
        
        ; mov ebx, 0
        ; mov bx, [b]
        ; div ebx
        
        ; mov ecx, ebx
        ; mul ecx
        
        ; add eax, ecx
        
        ; add ebx, 2
        ; div ebx
        
        ; add eax, [e]
        ; sub eax, [x]
        
        ; ex 22: a/2+b*b-a*b*c+e+x
        ; mov eax, 0
        ; mov al, [a]
        
        ; mov edx, 0
        ; mov ecx, 2
        ; div ecx
        
        ; mov ebx, 0
        ; mov bl, [b]
        ; mov edx, ebx
        ; mul ebx
        
        ; add eax, edx
        
        ; mov ebx, 0
        ; mov bl, [b]
        ; mov edx, 0
        ; mov dl, [c]
        ; mul dl
        ; mov ecx, eax
        ; mov eax, 0
        ; mov al, [a]
        ; mul ecx
        
        ; sub edx, eax
        ; add edx, [e]
        ; add edx, [x]
        
        ; ex 23: (a*b-2*c*d)/(c-e)+x/a
        ; mov eax, 0
        ; mov al, [a]
        ; mov bl, [b]
        ; mul bl
        
        ; mov al, [c]
        ; mov bl, [d]
        ; mul bl
        
        ; mov ebx, eax
        ; add eax, ebx
        
        ; sub ebx, eax
        
        ; mov al, [c]
        ; mov edx, 0
        ; mov dx, [e]
        ; sub eax, edx
        
        ; mov edx, 0
        ; div eax
        
        ; mov edx, 0
        ; mov eax, [x]
        ; mov bl, [a]
        ; div bl
        
        ; add ebx, eax
        
        ; ex 24: a-(7+x)/(b*b-c/d+2)
        ; mov eax, [a]
        
        ; mov edx, 0
        ; mov ecx, [x]
        ; add ecx, 7
        
        ; mov bl, [b]
        ; mov ebx, ebx
        ; mul bl
        
        ; mov edx, 0
        ; mov al, [c]
        ; mov bl, [d]
        ; div bl
        
        ; sub ebx, eax
        ; add ebx, 2
        
        ; mov edx, 0
        ; div ebx
        
        ; sub eax, [a]
        
        ; ex 30: a*b-(100-c)/(b*b)+e+x
        ; mov ax, [a]
        ; mov ebx, eax
        
        ; mov bl, [b]
        ; mul bl
        
        ; mov al, [c]
        ; sub eax, 100
        
        ; mov bl, [b]
        ; mov ecx, ebx
        ; mul bl
        
        ; mov edx, 0
        ; div ecx
        
        ; sub ebx, eax
        ; add ebx, [e]
        
        ; mov eax, [x]
        ; add ebx, eax
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
