#include <stdio.h>
#include <stdlib.h>
#include <stddef.h>
#include <string.h>
#include "queue.h"
#define MAX_NUM 1000

struct client_queue *belt;  
int front, rear;      //front is the position of the first element and rear is the position of the last element
int capacity, count;  /*capacity is the maximum number of elements that can be stored in the queue and count is the current number of 				elements in the queue*/

// To create a queue
int queue_init(int size){
	capacity = size;
	front = 0;
	rear = -1;
	count = 0;
	belt = malloc(capacity*sizeof(struct client_queue));   //malloc for allocating memory to the belt
	if(belt == NULL){
		return -1;
	}
	return 0;
}


// To Enqueue an element
char queue_put(struct client_queue* x) {
	if(check_ifexists(x->client_name) == 0){
		return 1;           //Already exists, then error
	}
	rear = (rear+1) % capacity; //each time I enqueue an element, I increase the rear to have the following position to enqueue
	belt[rear] = *x;
	count = count+1;            //increment the number of elements in the queue
	return 0;
}
	

// To Dequeue an element.(Used to get_value)
struct client_queue* queue_get(char* name) {
	struct client_queue *elast;
	elast = &belt[front];         //store the element to be dequeued to be returned later if it's the interested one
	front = (front+1) % capacity;
	count = count-1;              //decrease the number of elements in the queue
	
	if(strcmp(elast->client_name, name) == 0) { //return the element if it's the interested one  
		return elast;
	}
	else{
		queue_put(elast);         //if it is not the interested one, I enqueue the element again
		return queue_get(name);    //and I search the interested one again using recursion
	}
}


// To delete an element (the same as dequeue, but without returning a struct)
char queue_del(char* name) {
	struct client_queue *elast;
	if(check_ifexists(name) == -1){
		printf("s> UNREGISTER %s FAIL\n",name);	
		return 1;
	}
	elast = &belt[front];  //we store the element to be dequeued to be returned later
	front=(front+1) % capacity;
	count=count-1;         //we decrease the number of elements in the queue

	if(strcmp(elast->client_name, name) == 0) {
		return 0;
	}

	else{
		queue_put(elast);      //if the element is not the interested one, I enqueue the element again
		return queue_del(name); //and I search the interested one again using recursion
	}
}


// Return number of elements
int queue_cap(void){
	return count;
}

// To check queue state
int queue_empty(void){

	if(count == 0){    //if there are no elements, I return 1, which means queue is empty
		return 1;
	}
	return 0;
}

int queue_full(void){ 

	if(count == capacity){ //if the number of elements is equal to the capacity I return 1, which means the queue is full
		return 1;
	}
	return 0;
}

// To check if the key is already in the queue
int check_ifexists(char* name){
	int aux_front = front;
	int aux_rear = rear;
	int k;
	for (k = aux_front; k<=aux_rear; k++){
		if(strcmp(belt[k].client_name, name) == 0) {		
			return 0; //It is in the queue
		}
	}
	return -1; //It is not in the queue
}

// To destroy the queue and free the resources
int queue_destroy(void){
	free(belt);
	return queue_init(MAX_NUM);
}








