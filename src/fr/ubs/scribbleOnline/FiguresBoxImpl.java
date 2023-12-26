package fr.ubs.scribbleOnline;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import fr.ubs.scribble.Figure;
import fr.ubs.scribble.Figures;

public class FiguresBoxImpl implements FiguresBox {
    Figures figures;
    List<Callback> callbacks;
    @Override
    public void addFigure(Figure figure) {
        boolean exist = false;
        for (Figure f : this.figures) {
            if (f.getId()==(figure.getId())) {
                exist = true;
            }
        }
        if (!exist){
        this.figures.add(figure);
        System.out.println("Figure added : " + figure.toString());
        this.callbacks.forEach(c -> {
            try {
                c.edit(figure);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });}
    }

    @Override
    public Figure removeFigure(Figure figure) {
        this.figures.remove(figure);
        
        return figure;
    }

    public FiguresBoxImpl() {
        this.figures = new Figures();
        this.callbacks = new ArrayList<Callback>();
    }

    @Override
    public Figures getFigures() throws RemoteException {
        return this.figures;
    }

    @Override
    public void registerCallback(Callback callback) throws RemoteException {
        this.callbacks.add(callback);
    }

    @Override
    public void updateFigure(Figure figure) throws RemoteException {
        System.out.println("Figure updated : " + figure.toString());
        this.figures.forEach(f -> {
            if (f.getId()==(figure.getId())) {
                f = figure;
            }
        });
        this.callbacks.forEach(c -> {
            try {
                c.edit(figure);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

}
