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
    

; our code starts here
segment code use32 class=code
    start:
        ; ex 1
        ; mov al, 1
        ; add al, 9
        
        ; ex 4
        ; mov al, 5
        ; sub al, 6
        
        ; ex 9
        ; mov al, 3
        ; mov bl, 4
        ; mul bl
        
        ; ex 5
        ; mov al, 10
        ; mov ah, 0 ; extinde valoarea lui al la 16 biti
        ; mov bl, 4
        ; div bl
        
        ; ex 3
        ; mov ax, 128
        ; add ax, 128
        
        ; ex 22
        ; mov al, 16
        ; mov ah, 0
        ; mov bl, 4
        ; div bl
        
        ; ex 23
        ; mov ax, 256
        ; mov bx, 1
        ; mul bx
        
        ; ex 26
        ; mov al, 3
        ; sub al, 4
        
        ; ex 29
        ; mov al, 14
        ; mov ah, 0
        ; mov bl, 6
        ; div bl
        
        ; ex 30
        ; mov al, 11
        ; add al, 5
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
