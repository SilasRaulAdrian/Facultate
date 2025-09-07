//
// thread_4.c - Thread creation example
//
// pthread_create() - create a thread
// pthread_join()   - wait for termination of another thread
//

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define MAX_THR 100		// maximum number of threads

//
// The thread start routine
//
void* do_work(void* arg)
{
	int id = *(int *)arg;
	free(arg);				// dealocate HERE !

	printf("Eu sunt thread-ul %d\n", id);

	return NULL;
}


int main(int argc, char* argv[])
{
	int* id = NULL;
	pthread_t tid[MAX_THR];
	for (int i = 0; i < MAX_THR; i++)
	{
		id = malloc(sizeof(int));
		*id = i;

		pthread_create(&tid[i], NULL, do_work, id);
	}

	for (int i = 0; i < MAX_THR; i++)
	{
		pthread_join(tid[i], NULL);
	}

	return 0;
}
