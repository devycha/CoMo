
const chatRoomId = document.querySelector("#chatRoomId").value;
const chatName = document.querySelector("#chatName").value;
const myName = document.querySelector("#myName").value;
const eventSource = new EventSource(`http://localhost:8080/chat/${chatRoomId}`);

console.log(chatRoomId, chatName, myName);

let chatBox = document.querySelector("#chat-box");
let msgInput = document.querySelector("#chat-outgoing-msg");

eventSource.onmessage = (e) => {
    const data = JSON.parse(e.data);

    if (data.sender == "ssar") {
        initOutgoingMessage(data.msg, data.createdAt)
    } else {
        initIncomingMessage(data.msg, data.createdAt)
    }

}

document.querySelector("#chat-send").addEventListener("click", () => {
    addMessage();
})

document.querySelector("#chat-outgoing-msg").addEventListener("keydown", (e) => {
    if (e.keyCode == 13) {
        addMessage();
    }
})

function createSendMsg(msg, time) {
    return `<div class="sent_msg"><p>${msg}</p><span class="time_date">${time}</span></div>`
}

function createReceiveMsg(msg, time) {
    return `<div class="received_msg"><p>${msg}</p><span class="time_date">${time}</span></div>`
}

async function addMessage() {
    let chat = {
        sender: myName,
        receiver: chatName,
        msg: msgInput.value,
        roomNum: chatRoomId
    }

    await fetch(`http://localhost:8080/chat`, {
        method: "post",
        body: JSON.stringify(chat),
        headers: {
            "Content-Type": "application/json"
        }
    })
}

function initOutgoingMessage(msg, time) {
    let chatOutgoingBox = document.createElement("div");
    chatOutgoingBox.className = "outgoing_msg";
    const convertTime =
        time.substring(5, 10) + " | " + time.substring(11, 16)

    chatOutgoingBox.innerHTML = createSendMsg(msg, convertTime);

    chatBox.append(chatOutgoingBox);
    msgInput.value = "";
    document.documentElement.scrollTop = document.body.scrollHeight;
}

function initIncomingMessage(msg, time) {
    let chatOutgoingBox = document.createElement("div");
    chatOutgoingBox.className = "incoming_msg";
    const convertTime =
        time.substring(5, 10) + " | " + time.substring(11, 16)

    chatOutgoingBox.innerHTML = createReceiveMsg(msg, convertTime);

    chatBox.append(chatOutgoingBox);
    msgInput.value = "";
    document.documentElement.scrollTop = document.body.scrollHeight;
}