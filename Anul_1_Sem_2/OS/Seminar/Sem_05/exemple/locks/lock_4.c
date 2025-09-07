//
// lock_4.c - Using read-write lock object
//
// pthread_rwlock_init()    - initialize a read-write lock object
// pthread_rwlock_destroy() - destroy a read-write lock object
//
// pthread_rwlock_rdlock()
// pthread_rwlock_wrlock()
// pthread_rwlock_unlock()
//

#include <stdio.h>
#include <pthread.h>

#define MAX_READ  1000	// maximum number of readers
#define MAX_WRITE 100	// maximum number of writers

int seats = 100;        // initial number of seats
float price = 75.0f;    // initial price

pthread_rwlock_t rwl;   // a read-write lock

//
// Readers start routine
//
void* check(void* arg)
{
	int id = (int)arg;

	pthread_rwlock_rdlock(&rwl);		// lock for read

	printf("Reader %2d: Locuri %d, pret %f\n",
		id, seats, price * (2 - seats/100.0f));

	pthread_rwlock_unlock(&rwl);		// unlock

	return NULL;
}

//
// Writers start routine
//
void* buy(void* a)
{
	int id = (int)a;

	pthread_rwlock_wrlock(&rwl);		// lock for write

	printf("Writer %2d: Locul meu este %d, pret %f\n",
		id, seats, price * (2 - seats/100.0f));
	seats--;

	pthread_rwlock_unlock(&rwl);		// unlock

	return NULL;
}



int main(int argc, char* argv[])
{
	pthread_rwlock_init(&rwl, NULL);	// create the read-write lock

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
		pthread_join(tr[i], NULL);      // wait for readers to finish
	}

	for (int i = 0; i < MAX_WRITE; i++)
	{
		pthread_join(tw[i], NULL);      // wait for writers to finish
	}

	pthread_rwlock_destroy(&rwl);		// destroy the read-write lock

	return 0;
}
