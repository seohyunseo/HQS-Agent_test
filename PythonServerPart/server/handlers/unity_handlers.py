from flask_socketio import Namespace, emit
from flask import request

class UnityNamespace(Namespace):
    def on_connect(self):
        sid = request.sid
        print('[Unity] Client connected')
        emit('server_response', {'message': f'[Unity] Connected'}, broadcast=True)
        emit('message', f'[Server] Connected', broadcast=True, to=sid)

    def on_disconnect(self, sid):
        emit('server_response', {'message': f'[Unity] Disconnected: {sid}'}, broadcast=True)
        print('[Unity] Client disconnected')

    def on_message(self, data):
        print(f"[Unity] data: {data}")
        emit('server_response', {'message': f"[Unity] {data}"}, broadcast=True)

    def on_client_message(self, data):
        print(f'[Unity] data: {data}')
        emit('server_response', {'message': f"[Unity] {data}"})
