//
// lock_1.c - Using readers and writers without locks
//

#include <stdio.h>
#include <pthread.h>

#define MAX_READ  1000   // maximum number of readers
#define MAX_WRITE 100    // maximum number of writers

int seats = 100;        // initial number of seats
float price = 75.0f;    // initial price

//
// Readers start routine
//
void* check(void* arg)
{
	int id = (int)arg;
	printf("Reader %2d: Locuri %d, pret %f\n",
		id, seats, price * (2 - seats/100.0f));

	return NULL;
}

//
// Writers start routine
//
void* buy(void* arg)
{
	int id = (int)arg;
	printf("Writer %2d: Locul meu este %d, pret %f\n",
		id, seats, price * (2 - seats/100.0f));
	seats--;

	return NULL;
}


int main(int argc, char* argv[])
{
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

	return 0;
}
