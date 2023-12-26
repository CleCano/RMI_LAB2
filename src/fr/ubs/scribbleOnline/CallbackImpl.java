package fr.ubs.scribbleOnline;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import fr.ubs.scribble.Figure;

public class CallbackImpl extends UnicastRemoteObject implements Callback{

    ScribbleClient client;
    
    public CallbackImpl(ScribbleClient client) throws RemoteException {
        this.client = client;
    }

    @Override
    public void edit(Figure figure) throws RemoteException {
        boolean exist = false;
        Figure toRemove = null;
        for (Figure f : this.client.getCanvas().getFigures()) {
            if (f.getId()==(figure.getId())) {
                exist = true;
                toRemove = f;
            }
        }
        figure.setSelected(false);
        if (!exist){
            
        System.out.println("Figure added" + figure.getId());
        client.getCanvas().getFigures().add(figure);

        client.getCanvas().repaint();
        
            
        }else{
            client.getCanvas().getFigures().remove(toRemove);
            client.getCanvas().getFigures().add(figure);
            client.getCanvas().repaint();
        }
    }
    
}
