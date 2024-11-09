/* 3000quiz.c */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

char * the_questions[] = {
        "What is the capitol of Canada?",
        "What is the name of Anil's dog?",
        "How many feet in a mile?",
        "What is your name?",
        NULL
};

char * the_answers[] = {
        "Ottawa",
        "ENV: DOGNAME",
        "5280",
        "Bob",
        NULL
};

int askQuestions(char **question, char *answer[],
                 char *argv[], char *envp[])
{
        int score = 0;
        int q = 0;
        int c;
        int res;
        char response[100];
        
        while (question[q] && answer[q]) {
                printf("%s ", question[q]);
                c = scanf("%100s", response);
                if (c != EOF) {
                        if (strcmp(answer[q], response) == 0) {
                                printf("Correct!\n");
                                score++;
                        } else {
                                printf("Sorry, the answer is %s.\n", answer[q]);
                        }
                }
                q++;
        }
        
        return score;
}

int main(int argc, char *argv[], char *envp[])
{
        int score;
        int numQuestions = 4;

        score = askQuestions(the_questions, the_answers, argv, envp);

        printf("\nYour score is %d out of %d.\n", score, numQuestions);

        return 0;
}