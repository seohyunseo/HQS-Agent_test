from flask_socketio import Namespace, emit
from flask import request
import hqs_server.handlers.shared as shared

class UnityNamespace(Namespace):
    def on_connect(self):
        sid = request.sid
        shared.unity_sid = sid

        print('[Unity] Client connected')
        emit('server_response', {'message': f'[Unity] Connected'}, broadcast=True) # send to unity web client
        emit('message', f'[Server] Connected', broadcast=True, to=sid) # send to unity quest client

    def on_disconnect(self, sid):
        if shared.unity_sid == request.sid:
            shared.unity_sid = None

        emit('server_response', {'message': f'[Unity] Disconnected: {sid}'}, broadcast=True)
        print('[Unity] Client disconnected')

    def on_message(self, data):
        print(f"[Unity] data: {data}")
        emit('server_response', {'message': f"[Unity] {data}"}, broadcast=True)

    def on_client_message(self, data):
        print(f'[Unity] data: {data}')
        emit('server_response', {'message': f"[Unity] {data}"})

    def sendData(self, sid, event, data):
        if sid:
            emit(event, f'{data}', namespace='/android', broadcast=True,to=sid)
        else:
             emit('server_response', {'message': f"[Unity] Cannot send data to Android"}, broadcast=True)