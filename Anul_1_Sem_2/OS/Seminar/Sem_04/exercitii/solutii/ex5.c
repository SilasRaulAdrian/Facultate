#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>


int main(int argc, char* argv[])
{
	int pd[2];
	int res = pipe(pd);
	if (res == -1)
	{
		perror("pipe()");
		exit(EXIT_FAILURE);
	}

	pid_t pid = fork();
	if (pid == -1)
	{
		perror("fork()");
		exit(EXIT_FAILURE);
	}

	if (pid == 0)
	{
		close(pd[1]);

		while(1)
		{
			char op;
			read(pd[0], &op, sizeof(char));

			if (op == '\0')
				break;

			printf("[Child] op: %c\n", op);

			int a;
			read(pd[0], &a, sizeof(int));
			printf("[Child] a: %d\n", a);

			int b;
			read(pd[0], &b, sizeof(int));
			printf("[Child] b: %d\n", b);

			int c;
			if (op == '+')
				c = a + b;
			if (op == '-')
				c = a - b;

			printf("[Child] %d %c %d = %d\n", a, op, b, c);
		}

		close(pd[0]);
		exit(EXIT_SUCCESS);
	}

	close(pd[0]);
	
	FILE* fd = fopen("in.txt", "r");
	if (fd == NULL)
	{
		perror("fopen()");
		exit(1);
	}

	while(1)
	{
		char op;
		int a, b;
		int n = fscanf(fd, "%c %d %d\n", &op, &a, &b);
		//printf("[Parent] n: %d\n", n);

		if (n == -1)
		{
			op = '\0';
			write(pd[1], &op, sizeof(char));
			break;
		}

		//printf("[Parent] op: %c\n", op);
		write(pd[1], &op, sizeof(char));
		sleep(1);

		//printf("[Parent] a: %d\n", a);
		write(pd[1], &a, sizeof(int));
		sleep(1);

		//printf("[Parent] b: %d\n", b);
		write(pd[1], &b, sizeof(int));
		sleep(1);
	}

	wait(NULL);

	fclose(fd);
	close(pd[1]);

	return 0;
}
