#include <iostream>
#include <string>
using namespace std;
int as1, as2, as3, scelta, totassenze, pres1, pres2, pres3;
float perc1, perc2, perc3;
string nom1, nom2, nom3;
main()
{
//primo studente
cout<<"Inserisci il nome del 1^ studente ";
cin>>nom1;
do{
	cout<<"Inserisci il numero dei giorni di assenza del 1^ studente ";
	cin>>as1;
}while(as1>6 || as1<0);
pres1=6-as1;
perc1=(float)pres1*100/6;
//secondo studente
cout<<"Inserisci il nome del 2^ studente ";
cin>>nom2;
do{
	cout<<"Inserisci il numero dei giorni di assenza del 2^ studente ";
	cin>>as2;
}while(as2>6 || as2<0);
pres2=6-as2;
perc2=(float)pres2*100/6;
//terzo studente
cout<<"Inserisci il nome del 3^ studente ";
cin>>nom3;
do{
	cout<<"Inserisci il numero dei giorni di assenza del 3^ studente ";
	cin>>as3;
}while(as3>6 || as3<0);
pres3=6-as3;
perc3=(float)pres3*100/6;
totassenze=as1+as2+as3;
//menù
do{
	system("cls");
	cout<<"Cosa si vuole visualizzare?"<<endl;
	cout<<endl;
	cout<<"1. Visualizzare il nome dello studente con piu' assenze."<<endl;
	cout<<"2. Il numero totale di giorni di assenza registrati per il corso."<<endl;
	cout<<"3. Elenco dei nomi degli studenti sempre presenti."<<endl;
	cout<<"4. Percentuale di presenze di ogni studente."<<endl;
	cout<<"5. Fine elaborazione."<<endl;
	cout<<"Scelta: ";
	cin>>scelta;
	switch (scelta)
		{
		case 1: if(as1>as2 && as1>as3)
					cout<<nom1<<endl;
				if(as2>as1 && as2>as3)
					cout<<nom2<<endl;
				if(as3>as1 && as3>as2)
					cout<<nom3<<endl;
				system("pause");
				break;
		case 2: cout<<"Il numero totale di giorni di assenza registrati per il corso e' "<<totassenze<<endl;
				system("pause");
				break;
		case 3: if(as1==0)
					cout<<nom1<<endl;
				if(as2==0)
					cout<<nom2<<endl;
				if(as3==0)
					cout<<nom3<<endl;
				system("pause");
				break;
		case 4: cout<<"Lo studente "<<nom1<<" e' stato presente il "<<perc1<<"% delle volte."<<endl;
				cout<<"Lo studente "<<nom2<<" e' stato presente il "<<perc2<<"% delle volte."<<endl;
				cout<<"Lo studente "<<nom3<<" e' stato presente il "<<perc3<<"% delle volte."<<endl;
				system("pause");
				break;
		case 5: cout<<"FINE ELABORAZIONE"<<endl;
				break;
       default: cout<<"Scelta errata"<<endl;
       			system("pause");
       			break;
		}
}while(scelta!=5);
system("pause");
}