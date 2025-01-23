#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#define BUFFER_SIZE  1024*4
char* buffer;


char* string_concat(char* s1, char* s2)
{
    char* ns = malloc(strlen(s1) + strlen(s2) + 1);
    strcpy(ns, s1);
    strcat(ns, s2);
    return ns;
}

char* int2str(int n)
{
    char buffer[BUFFER_SIZE];
    int len = sprintf(buffer,"%d",n);
    char *ns = malloc(len + 1);
    sprintf(ns,"%d",n);
    return ns;
}

char* char2str(char c)
{
    char *ns = malloc(2);
    sprintf(ns, "%c", c);
    return ns;
}

    char* double2str(double f)
    {
        char buffer[BUFFER_SIZE];
        int len = sprintf(buffer,"%f", f);
        char *ns = malloc(len + 1);
        sprintf(ns, "%f", f);
        return ns;
    }

char* bool2str(int b)
{
    char* ns = NULL;
    if(b)
    {
        ns = malloc(5);
        strcpy(ns, "true");
    }
    else
    {
        ns = malloc(6);
        strcpy(ns, "false");
    }
    return ns;
}

void somma_fun (double input1, double input2, double *result);
void sottrazione_fun (double input1, double input2, double *result);
double moltiplicazione_fun (double input1, double input2);
double divisione_fun (double input1, double input2);
void tutteleoperazioni_fun (double input1, double input2, double *res1, double *res2, double *res3, double *res4);
char* stampa_fun (char* stringa);


char* operazione;
double input1, input2, answer, result, res1, res2, res3, res4;
int flag;
void somma_fun (double input1, double input2, double *result) {
*result=input1+input2;
}

void sottrazione_fun (double input1, double input2, double *result) {
*result=input1-input2;
}

double moltiplicazione_fun (double input1, double input2) {
return input1*input2;
}

double divisione_fun (double input1, double input2) {
if(input2==0)  {
printf("%s\n", "Errore: divisione per zero");
return 0.0;
}

return input1/input2;
}

void tutteleoperazioni_fun (double input1, double input2, double *res1, double *res2, double *res3, double *res4) {
somma_fun(input1, input2, &*res1);
sottrazione_fun(input1, input2, &*res2);
*res3=divisione_fun(input1, input2);
*res4=moltiplicazione_fun(input1, input2);
}

char* stampa_fun (char* stringa) {
return string_concat("Ciao! ", stringa);
}



int main() {
flag=1;
while (flag)  {
printf("%s", "Inserisci l'operazione da effettuare (somma, sottrazione, divisione, moltiplicazione, tutteleoperazioni): ");
buffer = (char*) malloc((1024*5)*sizeof(char));
scanf("%[^\n]", buffer);
getchar();
operazione= (char*) malloc((strlen(buffer) + 1) *sizeof(char));
strcpy(operazione,buffer);
free(buffer);

printf("%s", "Inserisci il primo input: ");
scanf("%lf", &input1);
getchar();
printf("%s", "Inserisci il secondo input: ");
scanf("%lf", &input2);
getchar();
printf("%s%s%s%f%s%f\n", "Hai scelto l'operazione ", operazione, " con gli argomenti ", input1, " e ", input2);
if(strcmp(operazione, "somma") == 0)  {
somma_fun(input1, input2, &result);
}
 else  {
if(strcmp(operazione, "sottrazione") == 0)  {
sottrazione_fun(input1, input2, &result);
}
 else  {
if(strcmp(operazione, "divisione") == 0)  {
result=divisione_fun(input1, input2);
}
 else  {
if(strcmp(operazione, "moltiplicazione") == 0)  {
result=moltiplicazione_fun(input1, input2);
}
 else  {
if(strcmp(operazione, "tutteleoperazioni") == 0)  {
tutteleoperazioni_fun(input1, input2, &res1, &res2, &res3, &res4);
}
 else  {
printf("%s\n", "Operazione non consentita");
}

}

}

}

}

if(strcmp(operazione, "tutteleoperazioni") != 0)  {
printf("%s%f\n", "Il risultato è: ", result);
}
 else  {
printf("%s\n", stampa_fun(string_concat(string_concat(string_concat(string_concat(string_concat(string_concat(string_concat(string_concat("I risultati delle 4 operazioni sono \n", double2str(res1)), "\n"), double2str(res2)), "\n"), double2str(res3)), "\n"), double2str(res4)), "\n")));
}

printf("%s", "Vuoi continuare? (1 yes/0 no): ");
scanf("%lf", &answer);
getchar();
if(answer==1)  {
flag=1;
}
 else  {
flag=0;
}

}

return 1; 
}
