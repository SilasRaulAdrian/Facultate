bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, printf, scanf               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
import printf msvcrt.dll
import scanf msvcrt.dll

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    a dd 0                     
    b dd 0                      
    result dd 0                 
    k dd 5                      
    format_in db '%d', 0        
    format_out db 'Rezultatul in baza 2: %s', 10, 0 
    prompt_a db 'Introduceti numarul a (baza 10): ', 0
    prompt_b db 'Introduceti numarul b (baza 10): ', 0
    binary_buffer times 33 db 0

; our code starts here
segment code use32 class=code
    convert_to_binary:
        mov ecx, 32                
        lea edi, [binary_buffer + 32] 
        mov byte [edi], 0          
    .loop:
        dec edi                     
        test eax, 1                
        jz .write_zero
        mov byte [edi], '1'         
        jmp .next
    .write_zero:
        mov byte [edi], '0'        
    .next:
        shr eax, 1                  
        loop .loop                  
        ret
    start:
        ; ...
        push dword prompt_a
        call [printf]
        add esp, 4
        
        push dword a
        push dword format_in
        call [scanf]
        add esp, 8
        
        push dword prompt_b
        call [printf]
        add esp, 4
        
        push dword b
        push dword format_in
        call [scanf]
        add esp, 8
        
        mov eax, [a]
        cdq
        mov ecx, [b]
        idiv ecx
        
        mov ebx, [k]
        imul eax, ebx
        
        mov [result], eax
        
        call convert_to_binary
        
        push dword binary_buffer
        push dword format_out
        call [printf]
        add esp, 8
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
