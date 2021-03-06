/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#include <memory.h> /* for memset */
#include "message_store.h"

/* Default timeout can be changed using clnt_control() */
static struct timeval TIMEOUT = { 25, 0 };

enum clnt_stat 
init_1(void *argp, int *clnt_res, CLIENT *clnt)
{
	return (clnt_call(clnt, INIT,
		(xdrproc_t) xdr_void, (caddr_t) argp,
		(xdrproc_t) xdr_int, (caddr_t) clnt_res,
		TIMEOUT));
}

enum clnt_stat 
register_1(arguments2 *argp, int *clnt_res, CLIENT *clnt)
{
	return (clnt_call(clnt, REGISTER,
		(xdrproc_t) xdr_arguments2, (caddr_t) argp,
		(xdrproc_t) xdr_int, (caddr_t) clnt_res,
		TIMEOUT));
}

enum clnt_stat 
unsubscribe_1(arguments2 *argp, int *clnt_res, CLIENT *clnt)
{
	return (clnt_call(clnt, UNSUBSCRIBE,
		(xdrproc_t) xdr_arguments2, (caddr_t) argp,
		(xdrproc_t) xdr_int, (caddr_t) clnt_res,
		TIMEOUT));
}

enum clnt_stat 
store_1(arguments1 *argp, int *clnt_res, CLIENT *clnt)
{
	return (clnt_call(clnt, STORE,
		(xdrproc_t) xdr_arguments1, (caddr_t) argp,
		(xdrproc_t) xdr_int, (caddr_t) clnt_res,
		TIMEOUT));
}

enum clnt_stat 
num_messages_1(arguments2 *argp, int *clnt_res, CLIENT *clnt)
{
	return (clnt_call(clnt, NUM_MESSAGES,
		(xdrproc_t) xdr_arguments2, (caddr_t) argp,
		(xdrproc_t) xdr_int, (caddr_t) clnt_res,
		TIMEOUT));
}

enum clnt_stat 
get_message_1(arguments2 *argp, char **clnt_res, CLIENT *clnt)
{
	return (clnt_call(clnt, GET_MESSAGE,
		(xdrproc_t) xdr_arguments2, (caddr_t) argp,
		(xdrproc_t) xdr_wrapstring, (caddr_t) clnt_res,
		TIMEOUT));
}

enum clnt_stat 
set_status_connected_1(client *argp, int *clnt_res, CLIENT *clnt)
{
	return (clnt_call(clnt, set_status_connected,
		(xdrproc_t) xdr_client, (caddr_t) argp,
		(xdrproc_t) xdr_int, (caddr_t) clnt_res,
		TIMEOUT));
}

enum clnt_stat 
set_status_disconnected_1(arguments2 *argp, int *clnt_res, CLIENT *clnt)
{
	return (clnt_call(clnt, set_status_disconnected,
		(xdrproc_t) xdr_arguments2, (caddr_t) argp,
		(xdrproc_t) xdr_int, (caddr_t) clnt_res,
		TIMEOUT));
}

enum clnt_stat 
get_status_1(arguments2 *argp, char *clnt_res, CLIENT *clnt)
{
	return (clnt_call(clnt, get_status,
		(xdrproc_t) xdr_arguments2, (caddr_t) argp,
		(xdrproc_t) xdr_char, (caddr_t) clnt_res,
		TIMEOUT));
}

enum clnt_stat 
get_reciever_info_1(arguments2 *argp, client *clnt_res, CLIENT *clnt)
{
	return (clnt_call(clnt, get_reciever_info,
		(xdrproc_t) xdr_arguments2, (caddr_t) argp,
		(xdrproc_t) xdr_client, (caddr_t) clnt_res,
		TIMEOUT));
}

enum clnt_stat 
get_all_messages_1(arguments2 *argp, client_messages *clnt_res, CLIENT *clnt)
{
	return (clnt_call(clnt, get_all_messages,
		(xdrproc_t) xdr_arguments2, (caddr_t) argp,
		(xdrproc_t) xdr_client_messages, (caddr_t) clnt_res,
		TIMEOUT));
}
