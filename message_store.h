/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#ifndef _MESSAGE_STORE_H_RPCGEN
#define _MESSAGE_STORE_H_RPCGEN

#include <rpc/rpc.h>

#include <pthread.h>

#ifdef __cplusplus
extern "C" {
#endif


struct arguments1 {
	char sender[30];
	char recipient[30];
	int message_identifier;
	char message[256];
	char file_name[256];
};
typedef struct arguments1 arguments1;

struct arguments2 {
	char user[30];
	int message_identifier;
};
typedef struct arguments2 arguments2;

struct pending_messages_rpc {
	char sender[256];
	char receiver[256];
	char message[256];
	u_int id;
};
typedef struct pending_messages_rpc pending_messages_rpc;

struct client_messages {
	struct pending_messages_rpc list[256];
};
typedef struct client_messages client_messages;

struct client {
	char user[128];
	int client_port;
	short sockaddr_in_sin_family;
	u_short sockaddr_in_sin_port;
	char sockaddr_in_sin_zerp[8];
	u_long in_addr_s_addr;
};
typedef struct client client;

#define message_store 0x31423456
#define message_storeVER 1

#if defined(__STDC__) || defined(__cplusplus)
#define INIT 1
extern  enum clnt_stat init_1(void *, int *, CLIENT *);
extern  bool_t init_1_svc(void *, int *, struct svc_req *);
#define REGISTER 2
extern  enum clnt_stat register_1(arguments2 *, int *, CLIENT *);
extern  bool_t register_1_svc(arguments2 *, int *, struct svc_req *);
#define UNSUBSCRIBE 3
extern  enum clnt_stat unsubscribe_1(arguments2 *, int *, CLIENT *);
extern  bool_t unsubscribe_1_svc(arguments2 *, int *, struct svc_req *);
#define STORE 4
extern  enum clnt_stat store_1(arguments1 *, int *, CLIENT *);
extern  bool_t store_1_svc(arguments1 *, int *, struct svc_req *);
#define NUM_MESSAGES 5
extern  enum clnt_stat num_messages_1(arguments2 *, int *, CLIENT *);
extern  bool_t num_messages_1_svc(arguments2 *, int *, struct svc_req *);
#define GET_MESSAGE 6
extern  enum clnt_stat get_message_1(arguments2 *, char **, CLIENT *);
extern  bool_t get_message_1_svc(arguments2 *, char **, struct svc_req *);
#define set_status_connected 7
extern  enum clnt_stat set_status_connected_1(client *, int *, CLIENT *);
extern  bool_t set_status_connected_1_svc(client *, int *, struct svc_req *);
#define set_status_disconnected 8
extern  enum clnt_stat set_status_disconnected_1(arguments2 *, int *, CLIENT *);
extern  bool_t set_status_disconnected_1_svc(arguments2 *, int *, struct svc_req *);
#define get_status 9
extern  enum clnt_stat get_status_1(arguments2 *, char *, CLIENT *);
extern  bool_t get_status_1_svc(arguments2 *, char *, struct svc_req *);
#define get_reciever_info 10
extern  enum clnt_stat get_reciever_info_1(arguments2 *, client *, CLIENT *);
extern  bool_t get_reciever_info_1_svc(arguments2 *, client *, struct svc_req *);
#define get_all_messages 11
extern  enum clnt_stat get_all_messages_1(arguments2 *, client_messages *, CLIENT *);
extern  bool_t get_all_messages_1_svc(arguments2 *, client_messages *, struct svc_req *);
extern int message_store_1_freeresult (SVCXPRT *, xdrproc_t, caddr_t);

#else /* K&R C */
#define INIT 1
extern  enum clnt_stat init_1();
extern  bool_t init_1_svc();
#define REGISTER 2
extern  enum clnt_stat register_1();
extern  bool_t register_1_svc();
#define UNSUBSCRIBE 3
extern  enum clnt_stat unsubscribe_1();
extern  bool_t unsubscribe_1_svc();
#define STORE 4
extern  enum clnt_stat store_1();
extern  bool_t store_1_svc();
#define NUM_MESSAGES 5
extern  enum clnt_stat num_messages_1();
extern  bool_t num_messages_1_svc();
#define GET_MESSAGE 6
extern  enum clnt_stat get_message_1();
extern  bool_t get_message_1_svc();
#define set_status_connected 7
extern  enum clnt_stat set_status_connected_1();
extern  bool_t set_status_connected_1_svc();
#define set_status_disconnected 8
extern  enum clnt_stat set_status_disconnected_1();
extern  bool_t set_status_disconnected_1_svc();
#define get_status 9
extern  enum clnt_stat get_status_1();
extern  bool_t get_status_1_svc();
#define get_reciever_info 10
extern  enum clnt_stat get_reciever_info_1();
extern  bool_t get_reciever_info_1_svc();
#define get_all_messages 11
extern  enum clnt_stat get_all_messages_1();
extern  bool_t get_all_messages_1_svc();
extern int message_store_1_freeresult ();
#endif /* K&R C */

/* the xdr functions */

#if defined(__STDC__) || defined(__cplusplus)
extern  bool_t xdr_arguments1 (XDR *, arguments1*);
extern  bool_t xdr_arguments2 (XDR *, arguments2*);
extern  bool_t xdr_pending_messages_rpc (XDR *, pending_messages_rpc*);
extern  bool_t xdr_client_messages (XDR *, client_messages*);
extern  bool_t xdr_client (XDR *, client*);

#else /* K&R C */
extern bool_t xdr_arguments1 ();
extern bool_t xdr_arguments2 ();
extern bool_t xdr_pending_messages_rpc ();
extern bool_t xdr_client_messages ();
extern bool_t xdr_client ();

#endif /* K&R C */

#ifdef __cplusplus
}
#endif

#endif /* !_MESSAGE_STORE_H_RPCGEN */
