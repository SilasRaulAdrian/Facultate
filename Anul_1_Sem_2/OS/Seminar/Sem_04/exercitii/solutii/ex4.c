/*
	Să se scrie un program C în care două procese comunică prin pipe.
	Procesul A va efectua, ÎN MOD REPETAT, următoarele operații:
	- va citi de la tastatură un simbol s (care poate fi '+' sau '-')
	și îl va trimite prin pipe procesului B;
	- va genera, apoi, două numere aleatoare și le va trimite prin pipe
	procesului B.
	Procesul B va efectua, ÎN MOD REPETAT, următoarele operații:
	- va citi din pipe simbolul s și cele două numere aleatoare;
	- va efectua operația aritmetică corespunzătoare și va scrie
	rezultatul fiecărei operații într-un fișier de tip text, astfel:

	45 + 55 = 100
	20 - 30 = -10
	...

	Execuția ambelor procese se încheie atunci când de la tastatură
	se citește s = '0'.
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>

int main(int argc, char* argv[])
{
	int pd[2];
	int ret = pipe(pd);
	if (ret == -1)
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

	// in procesul copil
	if (pid == 0)
	{
			close(pd[1]);

			FILE* fd = fopen("out.txt", "w");
			if (fd == NULL)
			{
				perror("fopen()");
				exit(1);
			}

		while(1)
		{
			char op;
			read(pd[0], &op, sizeof(char));
			printf("[Child] op: %c\n", op);

			if (op == 's')
				break;

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

			char line[100];
			sprintf(line, "%d %c %d = %d\n", a, op, b, c);
			fwrite(line, 1, strlen(line), fd);
		}

		fclose(fd);
		close(pd[0]);
		
		exit(EXIT_SUCCESS);
	}

	// in procesul parinte
	close(pd[0]);

	while(1)
	{
		char op;
		printf("Operatie: ");
		scanf("%c", &op);
		getchar();
		write(pd[1], &op, sizeof(char));

		if (op == 's')
				break;

		int a = rand()%100 + 1;
		write(pd[1], &a, sizeof(int));
		sleep(1);

		int b = rand()%100 + 1;
		write(pd[1], &b, sizeof(int));
		sleep(1);
	}

	wait(NULL);
	close(pd[1]);

	return 0;
}
