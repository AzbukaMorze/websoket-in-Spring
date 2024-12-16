'use strict';

const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');

let stompClient = null;
let nickname = null;
let fullname = null;
let selectedUserId = null;

function connect(event) {
    nickname = document.querySelector('#nickname').value.trim();
    fullname = document.querySelector('#fullname').value.trim();

    if (nickname && fullname) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    stompClient.subscribe(`/user/${nickname}/queue/messages`, onMessageReceived);
    stompClient.subscribe(`/user/public`, onMessageReceived);

    // register the connected user
    stompClient.send("/app/user.addUser",
        {},
        JSON.stringify({nickName: nickname, fullName: fullname, userStatus: 'ONLINE'})
    );
    document.querySelector('#connected-user-fullname').textContent = fullname;
    findAndDisplayConnectedUsers().then();
}

async function findAndDisplayConnectedUsers() {
    const connectedUsersResponse = await fetch('/users');
    let connectedUsers = await connectedUsersResponse.json();
    connectedUsers = connectedUsers.filter(user => user.nickName !== nickname);
    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach(user => {
        appendUserElement(user, connectedUsersList);
        if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
    });
}

function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.nickName;

    const userImage = document.createElement('img');
    userImage.src = '../img/user_icon.png';
    userImage.alt = user.fullName;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.fullName;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';

}

async function deleteMessage(messageId) {
    try {
        await fetch(`/messages/${messageId}`, {
            method: 'DELETE'
        });
        await fetchAndDisplayUserChat(); // Обновление чата
    } catch (error) {
        console.error('Failed to delete message:', error);
    }
}



function displayMessage(senderId, content, messageId, timestamp) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderId === nickname) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }

    // Добавляем содержимое сообщения
    const message = document.createElement('p');
    message.textContent = content;
    messageContainer.appendChild(message);

    // Добавляем дату сообщения
    if (timestamp) {
        const dateElement = document.createElement('span');
        dateElement.classList.add('timestamp');
        dateElement.textContent = new Date(timestamp).toLocaleString(); // Форматирование даты
        messageContainer.appendChild(dateElement);
    }

    // Добавляем обработчик для контекстного меню (только для отправителя)
    if (senderId === nickname) {
        messageContainer.addEventListener('contextmenu', (e) => showContextMenu(e, messageId, content));
    }

    chatArea.appendChild(messageContainer);
}


function showContextMenu(event, messageId, content) {
    event.preventDefault();

    const contextMenu = document.querySelector('#context-menu');
    contextMenu.style.top = `${event.pageY}px`;
    contextMenu.style.left = `${event.pageX}px`;
    contextMenu.classList.remove('hidden');

    const editOption = document.querySelector('#edit-message');
    const deleteOption = document.querySelector('#delete-message');

    editOption.onclick = () => openEditModal(messageId, content);  // Обработка редактирования через контекстное меню
    deleteOption.onclick = () => deleteMessage(messageId);

    // Скрываем меню при клике вне
    document.addEventListener('click', () => {
        contextMenu.classList.add('hidden');
    }, { once: true });
}

let currentMessageId = null;

function openEditModal(messageId, currentContent) {
    currentMessageId = messageId;
    document.getElementById('editMessageInput').value = currentContent;
    document.getElementById('editMessageModal').classList.remove('hidden');
}

function closeEditModal() {
    currentMessageId = null;
    document.getElementById('editMessageInput').value = '';
    document.getElementById('editMessageModal').classList.add('hidden');
}

document.getElementById('saveEditButton').addEventListener('click', async () => {
    const newContent = document.getElementById('editMessageInput').value.trim();
    if (newContent && currentMessageId) {
        try {
            const response = await fetch(`/messages/${currentMessageId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(newContent).trim()
            });

            if (response.ok) {
                closeEditModal();
                fetchAndDisplayUserChat(); // Обновление чата
            } else {
                console.error('Failed to edit message:', response.statusText);
            }
        } catch (error) {
            console.error('Failed to edit message:', error);
        }
    }
});



document.getElementById('cancelEditButton').addEventListener('click', closeEditModal);


async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${nickname}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content, chat.id, chat.timestamp);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}


function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessage = {
            senderId: nickname,
            recipientId: selectedUserId,
            content: messageInput.value.trim(),
            timestamp: new Date()
        };
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        displayMessage(nickname, messageInput.value.trim(), {}, chatMessage.timestamp);
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
    event.preventDefault();
}


async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);

    if (selectedUserId && selectedUserId === message.senderId) {
        displayMessage(message.senderId, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    if (selectedUserId) {
        document.querySelector(`#${selectedUserId}`).classList.add('active');
    } else {
        messageForm.classList.add('hidden');
    }

    // Преобразуем senderId в корректный селектор
    const notifiedUser = document.querySelector(`#user-${message.senderId}`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
        nbrMsg.textContent = '';
    }
    if (notification.content === null) {
        // Если контент null, удаляем сообщение из интерфейса
        removeMessageFromUI(notification.id);
    } else {
        // Иначе обновляем содержимое сообщения
        updateMessageInUI(notification.id, notification.content);
    }
}

function removeMessageFromUI(messageId) {
    const messageElement = document.getElementById(`message-${messageId}`);
    if (messageElement) {
        messageElement.remove();
    }
}

function updateMessageInUI(messageId, newContent) {
    const messageElement = document.getElementById(`message-${messageId}`);
    if (messageElement) {
        messageElement.querySelector('.message-text').innerText = newContent;
    }
}

function onLogout() {
    stompClient.send("/app/user.disconnectUser",
        {},
        JSON.stringify({nickName: nickname, fullName: fullname, userStatus: 'OFFLINE'})
        );
    window.location.reload();
}

usernameForm.addEventListener('submit', connect, true); // step 1
messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();
