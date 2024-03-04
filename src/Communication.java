import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Communication implements ICommunication
{
    final String SERVER_ADDRESS = "127.0.0.1"; // Replace with the server address, now it's local host
    final int PORT = 8080;

    PrintWriter writer;
    BufferedReader reader;

    public void OpenCommunication()
    {
        // open socket with Server
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMove(String move)
    {
        writer.println(move);
    }

    public String receiveMove()
    {
        String move;
        try {
            move = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return move;
    }
}
