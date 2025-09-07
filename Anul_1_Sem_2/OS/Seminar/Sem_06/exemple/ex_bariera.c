/*
	Să se determine cel mai mic număr întreg dintre 100.000 de numere aleatoare
	folosind 100 thread-uri. Să se determine și să se afișeze pe fiecare thread
	diferența absolută dintre minimul local și minimul global.
*/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <limits.h>

#define MAX_THR 100
#define MAX_NUM 100000

int min = INT_MAX;
int* numere = NULL;

pthread_barrier_t barr;
pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;

// thread start routine
void* do_work(void* arg)
{
	int id = *(int*)arg;
	free(arg);
	
	int local_min = INT_MAX;
	int st = id * (MAX_NUM/MAX_THR);
	int dr = st + (MAX_NUM/MAX_THR);
	for(int i = st; i < dr; i++)
	{
		if (numere[i] < local_min)
			local_min = numere[i];
	}
	
	pthread_mutex_lock(&mtx);
	if (local_min < min)
		min = local_min;
	pthread_mutex_unlock(&mtx);
	
	pthread_barrier_wait(&barr);
	
	int dif = abs(local_min - min);
	printf("[Thread %2d] Minim global: %d Diferenta: %d\n", id, min, dif);
	
	return NULL;
}

int main(int argc, char* argv[])
{
	// aloc dinamic tabloul de numere
	numere = malloc(MAX_NUM*sizeof(int));
	if(numere == NULL)
	{
		perror("malloc()");
		exit(1);
	}
	
	// generez numerele aleatoare
	srand(time(NULL));
	for(int i = 0; i < MAX_NUM; i++)
	{
		numere[i] = rand();
	}
	
	// determin minimul pentru verificare
    int minim = INT_MAX;
    for(int i = 0; i < MAX_NUM; i++)
    {
        if (minim > numere[i])
			minim = numere[i];
	}
    printf("Minim in main(): %d\n", minim);
	
	// creez bariera
	pthread_barrier_init(&barr, NULL, MAX_THR);
	
	// creez thread-urile
	pthread_t th[MAX_THR];
	for(int i = 0; i < MAX_THR; i++)
	{
		int* id = malloc(sizeof(int));
		*id = i;
		pthread_create(&th[i], NULL, do_work, id);
	}
	
	// astept terminarea thread-urilor
	for(int i = 0; i < MAX_THR; i++)
	{
		pthread_join(th[i], NULL);
	}
	
	// distrug bariera
	pthread_barrier_destroy(&barr);
	
	return 0;
}
