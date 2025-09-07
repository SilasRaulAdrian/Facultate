//
// no_lock.c
//
// Increment a variable in 100 threads without using locks
//

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

#define MAX_THR 100		// maximum number of threads

int val = 0;

//
// The thread start routine
//
void* do_work(void* arg)
{
    int id = *(int*)arg;
    free(arg);

    // int b = val;
    // usleep(100);
    // val = b + 1;

    val = val + 1;
    printf("Thread %2d - val: %d\n", id, val);

    return NULL;
}


int main(int argc, char* argv[])
{
    pthread_t tid[MAX_THR];				// an array of thread IDs
    for (int i = 0; i < MAX_THR; i++)
    {
        int* id = malloc(sizeof(int));
        *id = i;
        pthread_create(&tid[i], NULL, do_work, id);	// create a thread
	}

    for (int i = 0; i < MAX_THR; i++)
    {
        pthread_join(tid[i], NULL);		// wait for each thread to finish
	}

    return 0;
}
