# Rapport PDR

Valentin Moutteau & Xavier Naxara

## Contributions
### HDFS
La communication entre le client HDFS et les serveurs se font à l'aide de sockets. 

#### HDFS write
Le client envoie un flag HDFS_WRITE, puis le nom du fragment que le serveur doit copier localement (donc 
le nom du fichier à écrire avec l'information du numéro de fragment en plus), puis il envoie directement les 
KV, tant que l'on ne dépasse pas un seuil de bytes envoyés, qui correspond a la taille du fichier divisé 
par le nombre de serveurs.

#### HDFS delete
Le client envoie un flag HDFS_DELETE, le nom du fichier à supprimer, et le serveur supprime le premier 
fragment qu'il trouve (dans le cas où l'on aurait plusieurs fragments sur la même machine). Puis le serveur 
envoie un flag au client : 0 si tout s'est bien passé, -1 s'il n'a pas trouvé de fragments, et -2 si le fichier ne peut pas être supprimé.

#### HDFS read
Le client envoie un flag HDFS_READ, le nom du fichier à lire, avec l'information du numéro de fragment, ensuite le serveur renvoie -1 s'il ne trouve pas le fichier, ou le numéro du fragment, puis le fragment. On considère que la config n'as pas changé depuis l'utilisation de write, pour pouvoir reconstruire le fichier dans l'ordre.

### Application 

Pour faire le lien entre les maps et le reduce, on communique encore une fois par des sockets. On dispose d'une classe Adapter.java qui s'occupe de faire office de multiplexeur entre les workers et le reduce qui peut lire au fur et à mesure. Ceci est implanté avec une BlockingQueue. Grâce à la méthode getAdapterEntry() qui renvoie un NetworkReaderWriter, tout ce qui est écrit depuis le map est copié en parallèle (par un Thread dédié) dans la BlockingQueue. La méthode read() de cette classe permet de prendre le premier élément dans la queue.


## Manuel d'utilisation

### Configuration 

Dans le dossier config, on peut écrire un fichier de configuration qui suit la syntaxe suivante (exemple pour 2 nodes) : 
```
Machine1:Port_HDFS1:Port_RMI1
Machine2:Port_HDFS2:Port_RMI2

```
(Le dernier retour à la ligne est important si l'on utilise les scripts de déploiement, s'il n'est pas présent la dernière machine ne sera pas prise en compte).

On doit également changer dans le fichier src/config/Config.Java l'attribut CONFIG_FILE avec le nom du fichier de configuration à utiliser.

### Lancer l'application

On peut lancer/relancer/éteindre les serveurs HDFS avec : 
```bash
bash scripts/launch_hdfs.sh config/maConfig
bash scripts/restart_hdfs.sh config/maConfig
bash scripts/shutdown_hdfs.sh config/maConfig
```

Une fois les serveurs lancés, on peut enfin communiquer avec les serveurs HDFS pour écrire, lire, ou supprimer un fichier en executant la classe HdfsClient.java.

On peut aussi lancer l'application entière, c'est à dire les serveurs HDFS et les Workers Hagidoop avec : 

```bash
bash scripts/launch_app.sh config/maConfig
bash scripts/restart_app.sh config/maConfig
bash scripts/shutdown_app.sh config/maConfig
```

et lancer le comptage de mots en éxecutant la classe MyMapReduce.java.

### Netoyage 

On peut vider le dossier créé par HDFS et tous les fichiers qu'il contient pour une configuration donnée avec :

```bash
bash scripts/clean_hdfs.sh
```

## Evalution des performances

Pour un petit fichier (~ 10 mo), avec la méthode basique, on le réalise en moins d'une seconde, alors que la version map/reduce prend 8s : avec un petit fichier l'utilisation de map/reduce n'est pas intéressante. 

