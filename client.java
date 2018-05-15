import java.io.*;
import java.net.*;
import gnu.getopt.Getopt;


class client implements Runnable { // Implementing Runnable allowed me to create the new thread inside the client class itself

    /********************* TYPES **********************/

    /**
    * @brief Return codes for the protocol methods
    */
    private static enum RC {
        OK,
        ERROR,
        USER_ERROR
    };



    /******************* ATTRIBUTES *******************/

    private static String _server   = null;
    private static int _port = -1;


    private static ServerSocket receiveSocket;
    private static Boolean connected = false; 		//Boolean used to know if the client is connected or not
    private static String connectedUser = "";  //Store username of the connected user at the moment
    private static Boolean running = true; // Boolean used to control the execution of the client's thread



    /******************* CONSTRUCTOR (For the thread) *******************/

    public client(ServerSocket sc){
        this.receiveSocket = sc;
    }

    /********************* METHODS ********************/

    /**
    * @param user - User name to register in the system
    *
    * @return OK if successful
    * @return USER_ERROR if the user is already registered
    * @return ERROR if another error occurred
    */
    // FUNCTION THAT READS, BYTE PER BYTE, FROM THE DATAINPUTSTREAM PASSED AS PARAMETER, AND RETURNS THE APPENDED - FINAL STRING.
    public static String byteperbyte(DataInputStream a) throws IOException
    {
        String read = "";
        byte lastbyte = 'a';
        byte reader = 'a';
        while (lastbyte != '\0')
        {
            reader = a.readByte();
            read = read + (char) reader;
            lastbyte = reader;
        }
        return read;
    }

    // A simple method created for stopping the execution. It just sets the global variable 'running' to false.
    public static void stop()
    {
        running = false;
    }

    // This function is used when the client thread is running
    public void run() {
        try
        {
            receiveSocket.setSoTimeout(1000); // Set a timeout of 1 second for the receiving socket
        }
        catch(Exception e){}
        while(running == true)
        {
            try{
                Socket rSocket = receiveSocket.accept(); // Accept the connection from the server
                byte[] read = new byte[256];
                DataInputStream inFromServer = new DataInputStream(rSocket.getInputStream());
                String s = byteperbyte(inFromServer);
                if (s.equals("SEND_MESSAGE"+'\0')) // If the message received by the server is this one, then we receive the subsequent information sent by the server.
                {
                    String user = byteperbyte(inFromServer);
                    String msgId = byteperbyte(inFromServer);
                    String message = byteperbyte(inFromServer);
                    System.out.println("c> MESSAGE " + msgId +" FROM " + user + ":\n" + message + "\nEND");
                    rSocket.close(); // We close the receiving socket.
                }
                else if(s.equals("SEND_MESS_ACK"+'\0')) // If the message is this one, then we just receive the messageid afterwards.
                {
                    String msgId = byteperbyte(inFromServer);
                    System.out.println("SEND MESSAGE " + msgId + " OK");
                    rSocket.close(); // We close the receiving socket.
                }
                else // In case the client detects any other message sent by the server, then we print an error.
                {
                    System.out.println("c> Unknown command received. Message: " + s + "\nc> ");
                    rSocket.close(); // We close the receiving socket.
                }
            }
            catch(SocketTimeoutException e){}
            catch(IOException ie)
            {
                System.out.println("c> Message receiving thread finished execution. Connect again to restore.");
                ie.printStackTrace();
            }
            catch(Exception e){}
        }
    }
    // Method for registering a user
    static RC register(String user)
    {

        try {
            // Create socket and mechanisms to send to the server
            Socket clientSocket = new Socket(_server, _port);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            // Send the "REGISTER" command, and the username that wants to be registered, to the server.
            outToServer.writeBytes("REGISTER\0");
         //   outToServer.writeByte('\0');
            outToServer.writeBytes(user);
            outToServer.writeByte('\0');

            // Then, we read the response sent by the server.
            DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
            byte read = inFromServer.readByte();

            // We process/interpret the answer from the server and print the corresponding messages related to the response.
            switch(read){
                case 0:
                System.out.println("c> REGISTER OK");
                break;
                case 1:
                System.out.println("c> USERNAME IN USE");
                break;
                case 2:
                System.out.println("c> REGISTER FAIL");
                break;
            }
            //Close socket
            clientSocket.close();

        }
        catch(Exception e)
        {
            System.out.println("Exception: " + e);
            e.printStackTrace();
            return RC.ERROR;
        }
        return RC.OK;
    }

    /**
    * @param user - User name to unregister from the system
    *
    * @return OK if successful
    * @return USER_ERROR if the user does not exist
    * @return ERROR if another error occurred
    */

    static RC unregister(String user)
    {
        try {
            // Create socket and mechanisms to send
            Socket clientSocket = new Socket(_server, _port);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            // Send the "UNREGISTER" command, and the username that wants to be unregistered, to the server.
            outToServer.writeBytes("UNREGISTER");
            outToServer.writeByte('\0');
            outToServer.writeBytes(user);
            outToServer.writeByte('\0');

            // We process/interpret the answer from the server and print the corresponding messages related to the response.
            DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
            byte read = inFromServer.readByte();

            //Process answer from server and print corresponding messages
            switch(read){
                case 0:
                System.out.println("c> UNREGISTER OK");
                break;
                case 1:
                System.out.println("c> USER DOES NOT EXIST");
                break;
                case 2:
                System.out.println("c> UNREGISTER FAIL");
                break;
            }

            //Close socket
            clientSocket.close();

        }
        catch(Exception e)
        {
            System.out.println("Exception: " + e);
            e.printStackTrace();
            return RC.ERROR;
        }
        return RC.OK;
    }

    /**
    * @param user - User name to connect to the system
    *
    * @return OK if successful
    * @return USER_ERROR if the user does not exist or if it is already connected
    * @return ERROR if another error occurred
    */
    static RC connect(String user)
    {

        if(connected == false){ // Check if client already connected
            try {
                // Create socket and mechanisms to send to the server
                Socket clientSocket = new Socket(_server, _port);
                receiveSocket = new ServerSocket(0);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                // Send the "CONNECT" command, the username that wants to be connected, and the listening port for this client (which will be useful information for communicating to the server).
                outToServer.writeBytes("CONNECT");
                outToServer.writeByte('\0');
                outToServer.writeBytes(user);
                outToServer.writeByte('\0');
                outToServer.writeBytes(String.valueOf(receiveSocket.getLocalPort()));
                outToServer.writeByte('\0');

                // Then, we read the response sent by the server.
                DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
                byte [] read = new byte[1];
                inFromServer.read(read);

                // We process/interpret the answer from the server and print the corresponding messages related to the response.
                switch(read[0]){
                    case 0:
                    //On success
                    System.out.println("c> CONNECT OK");
                    running = true; // Set variable to control thread execution to true
                    new Thread(new client(receiveSocket)).start(); // Start execution of thread
                    connected = true; // Set connected boolean to false to prevent new connections on the same client
                    connectedUser = user; // Store local username
                    break;
                    case 1:
                    System.out.println("c> CONNECT FAIL, USER DOES NOT EXIST");
                    receiveSocket.close();
                    break;
                    case 2:
                    System.out.println("c> USER ALREADY CONNECTED");
                    receiveSocket.close();
                    break;
                    case 3:
                    System.out.println("c> CONNECT FAIL");
                    receiveSocket.close();
                    break;
                }

                //Close socket
                clientSocket.close();

            }
            catch(Exception e)
            {
                System.out.println("Exception: " + e);
                e.printStackTrace();
                return RC.ERROR;
            }
        }
        else
        { // If client is not connected:
             System.out.println("c> USER ALREADY CONNECTED");
        }
        return RC.OK;
    }

    /**
    * @param user - User name to disconnect from the system
    *
    * @return OK if successful
    * @return USER_ERROR if the user does not exist
    * @return ERROR if another error occurred
    */
    static RC disconnect(String user)
    {
        try {
            // Create socket and mechanisms to send to the server
            Socket clientSocket = new Socket(_server, _port);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            // Send the "DISCONNECT" command, and the username that wants to be disconnected, to the server.
            outToServer.writeBytes("DISCONNECT");
            outToServer.writeByte('\0');
            outToServer.writeBytes(user);
            outToServer.writeByte('\0');

            // Then, we read the response sent by the server.
            DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
            byte read = inFromServer.readByte();

            // We process/interpret the answer from the server and print the corresponding messages related to the response.
            switch(read){
                case 0:
                System.out.println("c> DISCONNECT OK");
                connected = false;	// Set connected = false to allow a ne connection
                connectedUser = ""; //Empty the connected username
                stop(); // Calls the function stop to stop message receiving thread
                break;
                case 1:
                System.out.println("c> DISCONNECT FAIL / USER DOES NOT EXIST");
                break;
                case 2:
                System.out.println("c> DISCONNECT FAIL / USER NOT CONNECTED");
                break;
                case 3:
                System.out.println("c> DISCONNECT FAIL");
                break;
            }

            //Close socket
            clientSocket.close();
        }catch(Exception e) {

            System.out.println("Exception: " + e);
            e.printStackTrace();
            return RC.ERROR;

        }

        return RC.OK;
    }

    /**
    * @param user    - Receiver user name
    * @param message - Message to be sent
    *
    * @return OK if the server had successfully delivered the message
    * @return USER_ERROR if the user is not connected (the message is queued for delivery)
    * @return ERROR the user does not exist or another error occurred
    */
    static RC send(String user, String message)
    {

        try {
            // Create socket and mechanisms to send to the server
            Socket clientSocket = new Socket(_server, _port);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            // Send the "SEND" command, the sender of the message, the receiver of the message, and the message itself, to the server.
            outToServer.writeBytes("SEND");
            outToServer.writeByte('\0');
            outToServer.writeBytes(connectedUser);
            outToServer.writeByte('\0');
            outToServer.writeBytes(user);
            outToServer.writeByte('\0');
            outToServer.writeBytes(message);
            outToServer.writeByte('\0');

            //Read answer from server
            DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
            byte [] read = new byte[1];
            inFromServer.read(read);


            // We process/interpret the answer from the server and print the corresponding messages related to the response.
            switch(read[0]){
                case 0:
                String msgId = byteperbyte(inFromServer);// Read again because on success, the server also sends the message id
                System.out.println("c> SEND OK - MESSAGE " + msgId);
                break;
                case 1:
                System.out.println("c> SEND FAIL / USER DOES NOT EXIST");
                break;
                case 2:
                System.out.println("c> SEND FAIL");
                break;
            }

            //Close socket
            clientSocket.close();

        }catch(Exception e) {

            System.out.println("Exception: " + e);
            e.printStackTrace();
            return RC.ERROR;

        }

        return RC.OK;
    }

    /**
    * @brief Command interpreter for the client. It calls the protocol functions.
    */
    static void shell() // The shell is executed during runtime to handle the processing of other methods, that are really in charge of sending the information to the server with the operation that wants to be performed.
    {
        boolean exit = false;
        String input;
        String [] line;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (!exit)
        {
            try
            {
                System.out.print("c> ");
                input = in.readLine();
                line = input.split("\\s");

                if (line.length > 0)
                {
                    /*********** REGISTER *************/
                    if (line[0].equals("REGISTER"))
                    {
                        if  (line.length == 2)
                        {
                            register(line[1]); // userName = line[1]
                        }
                        else
                        {
                            System.out.println("Syntax error. Usage: REGISTER <userName>");
                        }
                    }

                    /********** UNREGISTER ************/
                    else if (line[0].equals("UNREGISTER"))
                    {
                        if  (line.length == 2)
                        {
                            unregister(line[1]); // userName = line[1]
                        }
                        else
                        {
                            System.out.println("Syntax error. Usage: UNREGISTER <userName>");
                        }
                    }

                    /************ CONNECT *************/
                    else if (line[0].equals("CONNECT"))
                    {
                        if  (line.length == 2) {
                            connect(line[1]); // userName = line[1]
                        }
                        else
                        {
                            System.out.println("Syntax error. Usage: CONNECT <userName>");
                        }
                    }

                    /********** DISCONNECT ************/
                    else if (line[0].equals("DISCONNECT"))
                    {
                        if  (line.length == 2) {
                            disconnect(line[1]); // userName = line[1]
                        }
                        else
                        {
                            System.out.println("Syntax error. Usage: DISCONNECT <userName>");
                        }
                    }

                    /************** SEND **************/
                    else if (line[0].equals("SEND"))
                    {
                        if  (line.length >= 3)
                        {
				String message = line [2];
				for(int i = 3; i < line.length; i++){
					message = message + " " + line[i];

				}			

                                send(line[1], message);
                        }
                        else
                        {
                            System.out.println("Syntax error. Usage: SEND <userName> <message>");
                        }
                    }

                    /************** QUIT **************/
                    else if (line[0].equals("QUIT"))
                    {
                        if (line.length == 1)
                        {
                            exit = true;
                        }
                        else
                        {
                            System.out.println("Syntax error. Use: QUIT");
                        }
                    }

                    /************* IN CASE THE COMMAND IS NONE OF THE ABOVE: ************/
                    else
                    {
                        System.out.println("Error: command '" + line[0] + "' not valid.");
                    }
                }
            }
            catch (java.io.IOException e)
            {
                System.out.println("Exception: " + e);
                e.printStackTrace();
            }
        }
    }

    /**
    * @brief Prints program usage
    */
    static void usage() // Show the usage of the client
    {
        System.out.println("Usage: java -cp . client -s <server> -p <port>");
    }

    /**
    * @brief Parses program execution arguments
    */
    static boolean parseArguments(String [] argv)
    {
        Getopt g = new Getopt("client", argv, "ds:p:");

        int c;
        String arg;

        while ((c = g.getopt()) != -1) {
            switch(c) {
                //case 'd':
                //	_debug = true;
                //	break;
                case 's':
                _server = g.getOptarg();
                break;
                case 'p':
                arg = g.getOptarg();
                _port = Integer.parseInt(arg);
                break;
                case '?':
                System.out.print("getopt() returned " + c + "\n");
                break; // getopt() already printed an error
                default:
                System.out.print("getopt() returned " + c + "\n");
            }
        }

        if (_server == null)
            return false;

        if ((_port < 1024) || (_port > 65535))
        { // Check if the port number passed is a valid one.
            System.out.println("Error: Port must be in the range 1024 <= port <= 65535");
            return false;
        }
        return true;
    }



    /********************* MAIN **********************/

    public static void main(String[] argv)
    {
        if(!parseArguments(argv)) {
            usage();
            return;
        }


        shell();
        System.out.println("+++ FINISHED +++");
    }
}
