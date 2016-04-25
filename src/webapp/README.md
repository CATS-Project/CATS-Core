//Pour enregistrer un nouveau module auprès du serveur
//les types possibles pour les paramètres : bool,text,number (le type par défaut est 'text')
//Endpoint : Url du module distant
//name : Nom du module

Url : http://localhost:8080/module/register

{
	"endpoint":"http://local",
	"name":"Name module",
	"description":"htmlDescription",
	"returns":["html","data","none"],
	"params":[
		{
			"type":"text",
			"name":"param1",
			"displayName":"disp1"
		},
		{
			"type":"bool",
			"name":"param2",
			"displayName":"disp2"
		}
	]
}

===========
//Initier le traitement en appelant le 'init' du module distant, c'est le clic d'un utilisateur qui a déclencher ce traitement
//token : le token à garder par le module pour chaque requête (il doit exister sur toutes les requêtes envoyés par la suite au serveur)
//params : les valeurs des paramètres demandées par le module (ces valeurs sont remplies par l'utilisateur du CATS)
Url : http://endPointDuModule/init
{
	"token":"YFODIHF54sdf",
	"params":[
		{
			"param1":"ttatata",
			"param2":"true"
		}
	]
}

//Pour recuperer le corpus, le module doit effectuer des requêtes sur <URL_de_cats>/api en GET
//avec dans un header de la requete le token transmis précédemment. Vous pouvez aussi en parametre passer
//deux arguments "from" et "to" qui permette de paginer les requêtes et eviter d'avoir de trop grosse requêtes.
//Par exemple pour recuperer les tweet d'index entre 0 et 1000:
Url : http://urlDeCATS/api?to=1000&from=0
  header : { token : "slcjlsllsnklksnkknl" }
//Dés que la réponse contient moins d'objet que to-from, alors tout le corpus est récupéré.
//La reponse de cette requete est un simple tableau JSON contenant tout les tweet du corpus identifié par le token entre les index from et to. 
 

=======
//Envoyer le résultat du traitement fait par le module
//token : le token envoyé par le serveur (pour vérifier l'authenticité de la requête)
//result : Resultat du traitement format html

Url : http://localhost:8080/module/result
{
	"token":"YFODIHF54sdf",
	"result":"<html>.....</html>"
}
OU/ET
Url : http://localhost:8080/module/result/<TOKEN>
body :
[
	{tweet}
	...
	{tweet}
]
OU/ET
Url : http://localhost:8080/module/resultFile
body : 
{
	"token":"YFODIHF54sdf",
	"result":"...file..."
}