from flask_socketio import Namespace, emit

class AndroidNamespace(Namespace):
    def on_connect(self):
        print('[Android] Client connected')
        emit('server_response', {'message': 'Connected to server'})

    def on_disconnect(self):
        print('[Android] Client disconnected')

    def on_client_message(self, data):
        print('[Android] Received message from android client:', data)
        emit('server_response', {'message': f"Echo: {data['message']}"})