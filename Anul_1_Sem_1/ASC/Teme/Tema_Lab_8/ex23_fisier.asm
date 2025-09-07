bits 32

global start        

; Declarații de funcții externe necesare pentru program
extern exit, fopen, fprintf, fclose
import exit msvcrt.dll  
import fopen msvcrt.dll  
import fprintf msvcrt.dll
import fclose msvcrt.dll

; Secțiunea de date: variabile necesare programului
segment data use32 class=data
    nume_fisier db "numar_hex.txt", 0 ; Numele fișierului care va fi creat
    mod_acces db "w", 0               ; Modul de deschidere a fișierului (w - scriere)
    text_format db "%c", 0          ; Formatul pentru fprintf: un caracter urmat de newline
    hex_digits db "0123456789ABCDEF"  ; Cifrele hexazecimale
    numar dd 0x1234ABCD               ; Numărul pe 32 de biți ce va fi scris în fișier
    descriptor_fis dd -1              ; Descriptorul fișierului

; Codul programului începe aici
segment code use32 class=code
    start:
        ; Apelăm fopen pentru a crea/deschide fișierul
        ; eax = fopen(nume_fisier, mod_acces)
        push dword mod_acces     
        push dword nume_fisier
        call [fopen]
        add esp, 4*2                ; Eliberăm parametrii de pe stivă

        mov [descriptor_fis], eax   ; Salvăm descriptorul fișierului în variabila descriptor_fis
        
        ; Verificăm dacă fopen a creat cu succes fișierul (dacă EAX != 0)
        cmp eax, 0
        je final
        
        ; Lucrăm cu numărul și scriem fiecare cifră hexazecimală în fișier
        mov eax, [numar]            ; Încărcăm numărul în EAX
        mov ecx, 8                  ; 8 cifre hexazecimale (32 biți = 8 cifre)

    hex_loop:
        mov edx, eax                ; Copiem numărul în EDX
        shr edx, 28                 ; Mutăm cei mai semnificativi 4 biți la dreapta
        and edx, 0xF                ; Masca pentru a obține doar 4 biți (o cifră hexazecimală)
        
        ; Obținem cifra ca ASCII din hex_digits
        mov dl, [hex_digits + edx]  ; DL = caracterul corespunzător cifrei hexazecimale
        
        ; Scriem cifra în fișier folosind fprintf
        push edx                    ; Parametrul: caracterul (DL)
        push dword text_format      ; Parametrul: formatul (%c\n)
        push dword [descriptor_fis] ; Parametrul: descriptorul fișierului
        call [fprintf]              ; Apelăm fprintf
        add esp, 4*3                ; Eliberăm parametrii de pe stivă
        
        ; Pregătim pentru următoarea cifră
        shl eax, 4                  ; Eliminăm cei mai semnificativi 4 biți
        loop hex_loop               ; Repetăm pentru toate cele 8 cifre

        ; Apelăm fclose pentru a închide fișierul
        push dword [descriptor_fis] ; Parametrul: descriptorul fișierului
        call [fclose]
        add esp, 4

    final:
        ; Ieșim din program
        push    dword 0             ; Codul de ieșire: 0
        call    [exit]
