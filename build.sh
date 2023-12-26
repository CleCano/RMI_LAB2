if javac -d class  src/fr/ubs/scribble/*.java src/fr/ubs/scribble/shapes/*.java src/fr/ubs/scribbleOnline/*.java; then
    jar cvfe Client.jar fr.ubs.scribbleOnline.ScribbleClient -C class fr/ubs/scribble/ -C class fr/ubs/scribble/shapes/ -C class fr/ubs/scribbleOnline/
    jar cvfe Serveur.jar fr.ubs.scribbleOnline.ScribbleServeur -C class fr/ubs/scribble/ -C class fr/ubs/scribble/shapes/ -C class fr/ubs/scribbleOnline/
else
    echo "Compilation failed. Build stopped."
fi

java -cp Client.jar:triangles.jar -Djava.security.manager -Djava.rmi.server.useCodebaseOnly="false" -Djava.rmi.server.codebase="http://people.irisa.fr/Pascale.Launay/pub/triangles.jar" fr.ubs.scribbleOnline.ScribbleClient localhost 5000