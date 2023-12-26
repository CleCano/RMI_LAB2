package fr.ubs.scribbleOnline;

import java.rmi.Remote;
import java.rmi.RemoteException;

import fr.ubs.scribble.Figure;

public interface Callback extends Remote {
   
    public void edit(Figure figure) throws RemoteException;
}
