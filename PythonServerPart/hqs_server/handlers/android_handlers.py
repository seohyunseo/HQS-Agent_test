from flask_socketio import Namespace, emit
from flask import request
import hqs_server.handlers.shared as shared

class AndroidNamespace(Namespace):
    def on_connect(self):
        sid = request.sid
        shared.android_sid = sid

        print('[Android] Client connected')
        emit('server_response', {'message': f'[Android] Connected'},broadcast=True) # send to android web client
        emit('message', f'[Server] Connected', broadcast=True, to=sid) # send to android watch client
        
    def on_disconnect(self, sid):
        if shared.android_sid == request.sid:
            shared.android_sid = None

        emit('server_response', {'message': f'[Android] Disconnected: {sid}'}, broadcast=True)
        print('[Android] Client disconnected')

    def on_message(self, data):
        print(f"[Android] data: {data}")
        emit('server_response', {'message': f"[Android] {data}"}, broadcast=True)

    def on_IMU_message(self, data):
        emit('server_response', {'message': f"[Android] IMU: {data}"}, broadcast=True)
        self.sendData(shared.unity_sid, 'IMU_message', data)

    def on_HR_message(self, data):
        emit('server_response', {'message': f"[Android] HR: {data}"}, broadcast=True)
        self.sendData(shared.unity_sid, 'HR_message', data)

    def on_duration_message(self, data):
        emit('server_response', {'message': f"[Android] Set duration for IMU: {data}s"}, broadcast=True)
        self.sendData(shared.android_sid, 'duration_message', data, namespace='/android')
        

    def on_client_message(self, data):
        print(f'[Android] data: {data}')
        emit('server_response', {'message': f"[Android] {data}"})

    def sendData(self, sid, event, data, namespace='/unity'):
        if sid:
            emit(event, f'{data}', namespace=namespace, broadcast=True,to=sid)
        else:
             emit('server_response', {'message': f"[Android] Cannot send data to Unity"}, broadcast=True)
    
