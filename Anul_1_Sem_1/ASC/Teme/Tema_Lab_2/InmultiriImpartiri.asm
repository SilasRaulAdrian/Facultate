bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; a, b, c - byte, d - word
    ; a db 2
    ; b db 3
    ; c db 4
    ; d dw 5

    ; a, b, c, d - byte, e, f, g, h - word
    a db 2
    b db 3
    c db 4
    d db 5
    e dw 5
    f dw 4
    g dw 3
    h dw 2
; our code starts here
segment code use32 class=code
    start:
        ; a, b, c - byte, d - word
        
        ; ex 21
        ; mov eax, 0
        ; mov al, [a]
        ; add al, [b]
        ; add al, 2
        ; imul eax, eax, 3 ; eax = 3 * (a + b + 2)
        
        ; mov edi, eax
        
        ; mov ebx, 0
        ; mov bl, [c]
        ; add bl, 2
        ; imul ebx, ebx, 5 ; ebx = 5 * (c + 2)
        
        ; sub edi, ebx
        
        ; mov ax, [d]
        ; sub ax, di
        
        ; ex 22
        ; mov al, [a]
        ; imul eax, eax ; eax = a * a
        
        ; mov bl, [b]
        ; mov bh, bl
        ; add ebx, ebx ; ebx = 2 * b
        
        ; sub eax, ebx
        
        ; mov ax, [d]
        ; add eax, 10
        
        ; sub eax, eax
        
        ; mov al, [c]
        ; mov ebx, eax
        
        ; mov edx, 0
        ; div al
        
        ; ex 23
        ; mov al, [a]
        ; add al, [b]
        ; mov ah, 0
        ; imul eax, eax, 3
        
        ; mov bl, [c]
        ; add bl, bl
        
        ; sub eax, ebx
        ; add eax, [d]
        
        ; ex 24
        ; mov al, [a]
        ; mov ah, 0
        ; imul eax, eax, 10
        
        ; mov bl, [b]
        ; mov bh, 0
        ; imul ebx, ebx, 5
        
        ; sub eax, ebx
        
        ; mov edx, [d]
        ; mov cl, [c]
        ; imul ecx, ecx, 5
        ; sub edx, ecx
        
        ; add eax, edx
        
        ; ex 25
        ; mov eax, 0
        ; mov al, [a]
        ; mov ah, 0
        ; imul eax, eax, 10
        
        ; mov bl, [b]
        ; mov bh, 0
        ; mov cl, [c]
        ; add bl, cl
        ; imul ebx, ebx, 4
        
        ; add eax, ebx
        ; mov ecx, 100
        ; sub ecx, eax
        ; mov edx, [d]
        ; sub ecx, edx
        
        ; a, b, c, d - byte, e, f, g, h - word
        
        ; ex 26
        ; mov eax, 0
        ; mov ax, [e]
        ; add ax, [g]
        
        ; mov bl, [b]
        ; add bl, bl
        
        ; sub ax, bx
        ; mov bl, [c]
        
        ; mov dx, 0
        ; mov cx, ax
        ; div bl
        
        ; ex 27
        ; mov ax, [e]
        ; add ax, [f]
        ; sub ax, [g]
        
        ; mov bl, [b]
        ; add bl, [c]
        ; mov bh, bl        
        ; add bl, bh          
        ; add bl, bh
        
        ; add ax, bx
        
        ; mov cx, 5
        ; mov bx, ax
        
        ; mov dx, 0
        ; div cx
        
        ; ex 28
        ; mov ax, [e]
        ; add ax, [g]
        ; sub ax, [h]
        
        ; mov cx, 3
        ; mov dx, 0
        ; div cx
        
        ; mov bl, [b]
        ; mov bh, [c]
        ; mul bh
        
        ; add ax, bx
        
        ; ex 29
        ; mov al, [b]
        ; mov bl, [c]
        ; mul bl
        
        ; mov cx, [e]
        ; add cx, [f]
        
        ; sub ax, cx
        
        ; mov cl, [a]
        ; add cl, [d]
        
        ; mov dx, 0
        ; div cl
        
        ; ex 30
        ; mov al, [a]
        ; mov bl, 3
        ; mul bl
        
        ; mov cx, [e]
        ; add cx, [h]
        
        ; sub cx, ax
        
        ; mov eax, 100
        ; mov dx, 0
        ; div cx
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
