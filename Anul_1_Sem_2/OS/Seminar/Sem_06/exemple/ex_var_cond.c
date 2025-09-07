/*
	Să se determine suma a 100.000 de numere aleatoare utilizând 100 thread-uri
	producător și 100 thread-uri consumator. Thread-urile producator vor citi
	pe rând numerele aleatoare dintr-un fișier și le vor stoca într-un tablou
	care poate reține doar 1000 de numere.
*/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <limits.h>
#include <fcntl.h>
#include <unistd.h>

#define MAX_THR 100
#define MAX_SIZE 1000
#define MAX_NUM 100000

#define E_GOL 0
#define E_PLIN 1

int stare = E_GOL;

int fd = 0;
int suma = 0;
int buffer[MAX_SIZE];

pthread_cond_t vcc = PTHREAD_COND_INITIALIZER;
pthread_cond_t vcp = PTHREAD_COND_INITIALIZER;
pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;

void* produc(void* arg)
{
	int idp = *(int*)arg;
	free(arg);
	
	pthread_mutex_lock(&mtx);
	while (stare == E_PLIN)
	{
		printf("[Producator %2d] Ma pun in asteptare...\n", idp);
		pthread_cond_wait(&vcp, &mtx);
	}
	
	printf("[Producator %2d] Am inceput sa produc...\n", idp);
	
	for(int i = 0; i < MAX_SIZE; i++)
	{
		read(fd, &buffer[i], 2);
	}
	
	printf("[Producator %2d] Am terminat productia.\n", idp);
	
	stare = E_PLIN;
	pthread_cond_signal(&vcc);
	pthread_mutex_unlock(&mtx);
	
	return NULL;
}

void* consum(void* arg)
{
	int idc = *(int*)arg;
	free(arg);
	
	pthread_mutex_lock(&mtx);
	while (stare == E_GOL)
	{
		printf("[Consumator %2d] Ma pun in asteptare...\n", idc);
		pthread_cond_wait(&vcc, &mtx);
	}
	
	printf("[Consumator %2d] Am inceput consumul...\n", idc);
	
	for(int i = 0; i < MAX_SIZE; i++)
	{
		suma += buffer[i];
	}
	
	printf("[Consumator %2d] Am terminat consumul.\n", idc);
	
	stare = E_GOL;
	pthread_cond_signal(&vcp);
	pthread_mutex_unlock(&mtx);
	
	return NULL;
}

int main(int argc, char* argv[])
{
	// verific numarul de argumente
	if (argc < 2)
	{
		printf("Trebuie sa dai un nume de fisier !\n");
		exit(1);
	}
	
	// deschid fisierul
	fd = open(argv[1], O_RDONLY);
	if (fd < 0)
	{
		perror("open()");
		exit(2);
	}
	
	pthread_t tc[MAX_THR];
	pthread_t tp[MAX_THR];
	for(int i = 0; i < MAX_THR; i++)
	{
		int* idc = malloc(sizeof(int));
		*idc = i;
		pthread_create(&tc[i], NULL, consum, idc);
		int* idp = malloc(sizeof(int));
		*idp = i;
		pthread_create(&tp[i], NULL, produc, idp);
	}
	
	for(int i = 0; i < MAX_THR; i++)
	{
		pthread_join(tc[i], NULL);
		pthread_join(tp[i], NULL);
	}
	
	printf("Suma: %d\n", suma);
	
	close(fd);
	return 0;
}
