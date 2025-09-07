//
// lock_3.c - Using a semaphore
//
// sem_init()
// sem_destroy()
//
// sem_wait()
// sem_post()
//

#include <stdio.h>
#include <pthread.h>
#include <semaphore.h>

#define MAX_READ  1000	// maximum number of readers
#define MAX_WRITE 100	// maximum number of writers

int seats = 100;		// initial number of seats
float price = 75.0f;	// initial price

sem_t sem;				// a semaphore

//
// Readers start routine
//
void* check(void* arg)
{
	int id = (int)arg;

	sem_wait(&sem);		// lock

	printf("Reader %2d: Locuri %d, pret %f\n",
		id, seats, price * (2 - seats/100.0f));

	sem_post(&sem);		// unlock

	return NULL;
}

//
// Writers start routine
//
void* buy(void* arg)
{
	int id = (int)arg;

	sem_wait(&sem);		// lock

	printf("Writer %2d: Locul meu este %d, pret %f\n",
		id, seats, price * (2 - seats/100.0f));
	seats--;

	sem_post(&sem);		// unlock

	return NULL;
}


int main(int argc, char* argv[])
{
	sem_init(&sem, 0, 1);				// create the semaphore

	int j = 0;
	pthread_t tr[MAX_READ];				// reader TIDs
	pthread_t tw[MAX_WRITE];			// writer TIDs
	for (int i = 0; i < MAX_READ; i++)
    {
        pthread_create(&tr[i], NULL, check, (void*)i);

        if(i % 10 == 0 && j < MAX_WRITE)
        {
            pthread_create(&tw[j], NULL, buy, (void*)j);
            j++;
		}
	}

	for (int i = 0; i < MAX_READ; i++)
	{
		pthread_join(tr[i], NULL);		// wait for readers to finish
	}

	for (int i = 0; i < MAX_WRITE; i++)
	{
		pthread_join(tw[i], NULL);		// wait for writers to finish
	}

	sem_destroy(&sem);					// destroy the semaphore

	return 0;
}
