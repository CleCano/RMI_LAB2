if javac -d class  src/fr/ubs/scribble/*.java src/fr/ubs/scribble/shapes/*.java src/fr/ubs/scribbleOnline/*.java; then
    jar cvfe Client.jar fr.ubs.scribbleOnline.ScribbleClient -C class fr/ubs/scribbleOnline/ScribbleClient.class -C class fr/ubs/scribble -C class fr/ubs/scribble/shapes
    jar cvfe Serveur.jar fr.ubs.scribbleOnline.ScribbleServeur -C class fr/ubs/scribbleOnline/ScribbleServeur.class -C class fr/ubs/scribble -C class fr/ubs/scribble/shapes
else
    echo "Compilation failed. Build stopped."
fi
