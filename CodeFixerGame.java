import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CodeFixerGame extends Remote {
    String registerPlayer(String username, String level) throws RemoteException;
    String fetchChallenge(String username) throws RemoteException;
    String submitFix(String username, String solution) throws RemoteException;
    String getLeaderboard() throws RemoteException;
}
