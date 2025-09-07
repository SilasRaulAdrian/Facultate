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
    ; a resb 1
    ; b resb 1
    ; c resb 1
    ; d resb 1
    
    ; a resw 1
    ; b resw 1
    ; c resw 1
    ; d resw 1

; our code starts here
segment code use32 class=code
    start:
        ; a, b, c, d - byte
        
        ; ex 21
        ; mov al, [a]
        ; mov bl, [b]
        ; mov cl, [c]
        ; mov dl, [d]
        
        ; mov al, 3
        ; mov bl, 2
        ; mov cl, 5
        ; mov dl, 4
        
        ; sub al, bl
        ; sub cl, dl
        
        ; add al, cl
        
        ; ex 22
        ; mov al, [a]
        ; mov bl, [b]
        ; mov cl, [c]
        ; mov dl, [d]
        
        ; mov al, 3
        ; mov bl, 2
        ; mov cl, 1
        ; mov dl, 2
        
        ; add al, bl
        ; add al, bl
        ; add cl, dl
        
        ; sub al, cl
        
        ; ex 23
        ; mov al, [a]
        ; mov bl, [b]
        ; mov cl, [c]
        ; mov dl, [d]
        
        ; mov al, 5
        ; mov bl, 1
        ; mov cl, 3
        ; mov dl, 2
        
        ; sub al, cl
        ; add bl, bl
        ; add bl, dl
        
        ; add al, bl
        
        ; ex 24
        ; mov al, [a]
        ; mov bl, [b]
        ; mov cl, [c]
        ; mov dl, [d]
        
        ; mov al, 7
        ; mov bl, 1
        ; mov cl, 2
        ; mov dl, 1
        
        ; mov ah, al
        ; sub ah, bl
        ; sub ah, bl
        ; sub ah, cl
        
        ; mov al, al
        ; sub al, cl
        ; sub al, cl
        ; sub al, dl
        
        ; add al, ah
        
        ; ex 25
        ; mov al, [a]
        ; mov bl, [b]
        ; mov cl, [c]
        ; mov dl, [d]
        
        ; mov al, 1
        ; mov bl, 2
        ; mov cl, 3
        ; mov dl, 2
        
        ; add cl, dl
        ; add cl, dl
        ; add al, al
        ; add al, bl
        
        ; sub cl, al
        
        
        ; a, b, c, d - word
        
        ; ex 26
        ; mov ax, [a]
        ; mov bx, [b]
        ; mov cx, [c]
        ; mov dx, [d]
        
        ; mov ax, 5
        ; mov bx, 2
        ; mov cx, 4
        ; mov dx, 3
        
        ; add ax, cx
        ; add bx, bx
        ; add bx, dx
        
        ; sub ax, bx
        
        ; ex 27
        ; mov ax, [a]
        ; mov bx, [b]
        ; mov cx, [c]
        ; mov dx, [d]
        
        ; add ax, bx
        ; add cx, dx
        ; sub ax, cx
        ; add ax, 100h
        
        ; ex 28
        ; mov ax, [a]
        ; mov bx, [b]
        ; mov cx, [c]
        ; mov dx, [d]
        
        ; mov ax, 1
        ; mov bx, 3
        ; mov cx, 2
        ; mov dx, 4
        
        ; mov ax, dx
        ; sub ax, cx
        ; add bx, bx
        ; sub bx, cx
        ; sub bx, ax
        ; add ax, bx
        ; add ax, dx
        
        ; ex 29
        ; mov ax, [a]
        ; mov bx, [b]
        ; mov cx, [c]
        ; mov dx, [d]
        
        ; mov ax, 2
        ; mov bx, 1
        ; mov cx, 2
        ; mov dx, 4
        
        ; sub dx, ax
        ; add bx, bx
        ; add bx, cx
        ; add dx, bx
        
        ; ex 30
        ; mov ax, [a]
        ; mov bx, [b]
        ; mov cx, [c]
        ; mov dx, [d]
        
        ; mov ax, 5
        ; mov bx, 4
        ; mov cx, 3
        ; mov dx, 2
        
        ; mov si, ax
        ; sub si, bx
        ; sub cx, dx
        ; add cx, ax
        ; add si, cx
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
