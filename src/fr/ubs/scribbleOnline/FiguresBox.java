package fr.ubs.scribbleOnline;

import java.rmi.Remote;
import java.util.List;

import fr.ubs.scribble.Figure;
import fr.ubs.scribble.Figures;

public interface FiguresBox extends Remote{
    
    void addFigure(Figure figure) throws java.rmi.RemoteException;
    Figure removeFigure(Figure figure) throws java.rmi.RemoteException;
    Figures getFigures() throws java.rmi.RemoteException;
    void updateFigure(Figure figure) throws java.rmi.RemoteException;
    void registerCallback(Callback callback) throws java.rmi.RemoteException;
}
