struct arguments1 {
char sender[30];
char  recipient[30];
int message_identifier;
char message [256];
char file_name [256];
};

struct arguments2{
char user[30];
int message_identifier;
};

struct pending_messages_rpc{
	char sender[256];
	char receiver[256];
	char message[256];
	unsigned int id;
};

struct client_messages{
struct pending_messages_rpc list[256];
};

struct client { 
	char user[128];
	int client_port;
	short sockaddr_in_sin_family;
	unsigned short sockaddr_in_sin_port;
	char sockaddr_in_sin_zerp[8];
	unsigned long in_addr_s_addr;

};

program message_store {
	version message_storeVER {
		int INIT(void) = 1;
		int REGISTER(arguments2) = 2;
		int UNSUBSCRIBE(arguments2) = 3;
		int STORE(arguments1) = 4;
		int NUM_MESSAGES(arguments2) = 5;
		string GET_MESSAGE(arguments2) = 6; 
		int set_status_connected(client) = 7;
		int set_status_disconnected(arguments2) = 8;
		char get_status(arguments2) = 9;
		client get_reciever_info(arguments2) = 10;
		client_messages get_all_messages(arguments2) = 11;
			} = 1;



} = 0x31423456;
