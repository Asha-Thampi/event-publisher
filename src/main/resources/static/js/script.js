'use strict';
document.querySelector('#welcomeForm').addEventListener('submit', connect, true)
document.querySelector('#dialogueForm').addEventListener('submit', sendMessage, true)

var stompClient = null;
var name = null;

// Connect to the end point of the spring application
function connect(event) {
	name = document.querySelector('#name').value.trim();
	if (name) {
		document.querySelector('#welcome-page').classList.add('hidden');
		document.querySelector('#dialogue-page').classList.remove('hidden');

        // Initialize the variables
		var socket = new SockJS('/websocketApp');
		stompClient = Stomp.over(socket);
		stompClient.connect({}, connectionSuccess);
	}
	event.preventDefault();
}

// If connection is success, subscribe to the topic
function connectionSuccess() {
	stompClient.subscribe('/topic/brokerMessage', onMessageReceived);
	stompClient.send("/app/event.newEvent", {}, JSON.stringify({
		name : name,
		id : 'new_event'
	}))
}

// Execute when a message has to be sent
function sendMessage(event) {
	var messageContent = document.querySelector('#eventMessage').value.trim();
	if (messageContent && stompClient) {
		var eventMessage = {
			name : name,
			message : document.querySelector('#eventMessage').value,
			id : 'Message'
		};
		stompClient.send("/app/event.sendMessage", {}, JSON
				.stringify(eventMessage));
		document.querySelector('#eventMessage').value = '';
	}
	event.preventDefault();
}

// Execute when a message is received
function onMessageReceived(payload) {

	var messageBody = JSON.parse(payload.body);
	var messageElement = document.createElement('li');

	if (messageBody.id === 'new_event') {
    		messageElement.classList.add('event-data');
    		messageBody.message = '';
    }
    else if (messageBody.id === 'old_event') {
    		messageElement.classList.add('event-data');
    		messageBody.message = '';
    }
    else {
    		messageElement.classList.add('message-data');

    		var element = document.createElement('i');
    		var text = document.createTextNode(messageBody.name[0]);
    		element.appendChild(text);
    		messageElement.appendChild(element);

    		var usernameElement = document.createElement('span');
    		var usernameText = document.createTextNode(messageBody.name);
    		usernameElement.appendChild(usernameText);
    		messageElement.appendChild(usernameElement);

    	}

    	var textElement = document.createElement('p');
    	var messageText = document.createTextNode(messageBody.message);
    	textElement.appendChild(messageText);
    	messageElement.appendChild(textElement);

    	document.querySelector('#messageList').appendChild(messageElement);
    	document.querySelector('#messageList').scrollTop = document
    			.querySelector('#messageList').scrollHeight;
}