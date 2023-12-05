package fr.ubs.scribbleOnline;

import java.rmi.RemoteException;
import java.util.List;

import fr.ubs.scribble.Figure;
import fr.ubs.scribble.Figures;

public class FiguresBoxImpl implements FiguresBox {
    Figures figure;
    @Override
    public void addFigure(Figure figure) {
        this.figure.add(figure);
    }

    @Override
    public Figure removeFigure(Figure figure) {
        this.figure.remove(figure);
        return figure;
    }
    public FiguresBoxImpl() {
        this.figure = new Figures();
    }

    @Override
    public Figures getFigures() throws RemoteException {
        return this.figure;
    }
}
