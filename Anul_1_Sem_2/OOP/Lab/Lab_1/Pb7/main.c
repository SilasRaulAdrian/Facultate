#include <stdio.h>

int Exponent_prime_in_factorial(int n, int p)
{
    /* Functia calculeaza exponentul lui p in descompunerea factorialului lui n */
    int exponent = 0;
    while (n >= p)
    {
        exponent += n / p;
        n /= p;
    }
    return exponent;
}

double Radical_Approx(double n, double precision)
{
    /* Functia calculeaza radicalul unui numar n cu o precizie data */

    if (n < 0)
    {
        printf("Numarul trebuie sa fie pozitiv!\n");
        return -1;
    }

    double guess = n;
    double new_guess = 0.5 * (guess + n / guess);

    while (new_guess - guess > precision || guess - new_guess > precision)
    {
        guess = new_guess;
        new_guess = 0.5 * (guess + n / guess);
    }

    return new_guess;
}


int main(void)
{
    while(1)
    {
        int nr_problema;
        printf("Introduceti numarul problemei (7 sau 8) sau 0 pentru a iesi: ");
        scanf("%d", &nr_problema);
        if (nr_problema == 7)
        {
            double n, precision;
            printf("Introduceti un numar pozitiv: ");
            scanf("%lf", &n);
            printf("Introduceti precizia: ");
            scanf("%lf", &precision);

            double result = Radical_Approx(n, precision);
            if (result != -1)
                printf("Radicalul numarului %.2lf este aproximativ %.2lf\n", n, result);
        }
        else if (nr_problema == 8)
        {
            int n, p;
            printf("Introduceti un numar natural nenul n: ");
            scanf("%d", &n);
            printf("Introduceti un numar prim p: ");
            scanf("%d", &p);

            int exponent = Exponent_prime_in_factorial(n, p);
            printf("Exponentul lui %d in descompunerea factorialului lui %d este %d\n", p, n, exponent);
        }
        else if (nr_problema == 0)
            break;
    }

    return 0;
}
