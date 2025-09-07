#include <stdio.h>
#include "teste/teste.h"
#include "ui/consola.h"

int main() {
    printf("Se ruleaza testele...\n");
    run_all_tests();
    printf("Testele au fost trecute cu succes!\n");
    run();
    return 0;
}