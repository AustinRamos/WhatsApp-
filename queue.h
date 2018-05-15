#define MAXMESSAGE 256
#include <netinet/in.h>
#define CONNECTED 1
#define DISCONNECTED 0

struct pending_messages {
	char sender [MAXMESSAGE];
	char receiver [MAXMESSAGE];
	char message [MAXMESSAGE];
	unsigned int id;
};


struct client_queue { //structure used to store the information of the clients in the queue
	char client_name[128];
	char status;
	struct sockaddr_in client_address;
	int client_port;
	struct pending_messages list[MAXMESSAGE];
	int num_messages;
};



struct arguments_thread {
	int client_sock;
	struct sockaddr_in client_address;
char * host;
};

int queue_init (int size);
int check_ifexists(char* name);
int queue_destroy (void);
char queue_put (struct client_queue* elem);
struct client_queue * queue_get(char *name);
char queue_del(char *name);
int queue_cap(void);
int queue_empty (void);
int queue_full(void);


