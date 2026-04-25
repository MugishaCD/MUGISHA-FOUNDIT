// chat.js - Handles real-time messaging between users
		
document.addEventListener('DOMContentLoaded', () => {
    const chatForm = document.getElementById('chat-form');
    const messageInput = document.getElementById('message-content');
    const messagesContainer = document.getElementById('chat-messages');
    
    // Get matchId from URL parameters
    const urlParams = new URLSearchParams(window.location.search);
    const matchId = urlParams.get('matchId');
    
    if (!matchId) {
        showToast('No match ID found in URL', 'error');
        return;
    }

    document.getElementById('match-id-badge').innerText = `Match #${matchId}`;

    /**
     * Load messages for this match
     */
    async function loadMessages() {
        try {
            const messages = await api.get(`/messages/${matchId}`);
            renderMessages(messages);
        } catch (error) {
            console.error('Failed to load messages:', error);
        }
    }

    /**
     * Render message list
     */
    function renderMessages(messages) {
        if (!messages || messages.length === 0) return;
        
        messagesContainer.innerHTML = '';
        const currentUserId = JSON.parse(localStorage.getItem('user'))?.id;

        messages.forEach(msg => {
            const msgDiv = document.createElement('div');
            const isSent = msg.senderId === currentUserId;
            msgDiv.className = `message ${isSent ? 'sent' : 'received'}`;
            
            msgDiv.innerHTML = `
                <div class="message-text">${msg.content}</div>
                <span class="message-info">${new Date(msg.timestamp).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}</span>
            `;
            messagesContainer.appendChild(msgDiv);
        });
        
        // Scroll to bottom
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }

    /**
     * Send a new message
     */
    async function sendMessage(e) {
        e.preventDefault();
        const content = messageInput.value.trim();
        
        if (!content) return;

        try {
            messageInput.value = '';
            await api.post(`/messages/${matchId}`, content);
            await loadMessages(); // Refresh chat
        } catch (error) {
            showToast('Failed to send message', 'error');
        }
    }

    // Initial load
    loadMessages();
    
    // Poll for new messages every 3 seconds for basic "real-time" feel
    const pollInterval = setInterval(loadMessages, 3000);

    // Event listeners
    chatForm.addEventListener('submit', sendMessage);

    // Clean up on leave
    window.addEventListener('beforeunload', () => clearInterval(pollInterval));
});
