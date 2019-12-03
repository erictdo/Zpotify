package main.java.acp;

import main.java.dfs.Chord;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;

public interface AtomicCommandInterface {
    public boolean canCommit(Transaction t, int guid) throws RemoteException, FileNotFoundException;
    public void doCommit(Transaction t) throws RemoteException;
    public void doAbort(Transaction t,int guid) throws RemoteException;
    public void haveCommitted(Transaction t, Object p) throws RemoteException;
    public boolean getDecision(Transaction t) throws RemoteException;
}
