//
// ex1_1.c - Fara sincronizare
//
// Generati 100.000 de numere aleatoare si calculati suma lor utilizand 100 thread-uri.
//

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define MAX_THR 100
#define MAX_NUM 100000

int sum = 0;
int numbers[MAX_NUM];


// Rutina de start
void* do_sum(void *arg)
{
	int id = *(int *)arg;
	//printf("id = %d\n", id);
	free(arg);
	
	int min = id * (MAX_NUM/MAX_THR);
	int max = min + (MAX_NUM/MAX_THR);
	for (int i = min; i < max; i++)
	{
		sum += numbers[i];
	}

	return NULL;
}


int main(int argc, char *argv[])
{
	srand(time(NULL));
	
	// generez 100.000 numere aleatoare
	for (int i = 0; i < MAX_NUM; i++)
	{
		numbers[i] = rand() % 20;
	}

	// creez cele 100 thread-uri
	pthread_t tid[MAX_THR];
	for (int i = 0; i < MAX_THR; i++)
	{
		int *n = (int *)malloc(sizeof(int));
		*n = i;
		pthread_create(&tid[i], NULL, do_sum, n);
	}

	// astept terminarea thread-urilor
	for (int i = 0; i < MAX_THR; i++)
	{
		pthread_join(tid[i], NULL);
	}

	// afisez suma calculata de thread-uri
	printf("Suma calculata:  %d\n", sum);

	// verific suma
	int check_sum = 0;
	for (int i = 0; i < MAX_NUM; i++)
	{
		check_sum += numbers[i];
	}

	printf("Suma verificata: %d\n", check_sum);

	return 0;
}
