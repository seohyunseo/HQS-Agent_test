from flask_socketio import Namespace, emit

class AndroidNamespace(Namespace):
    def on_connect(self):
        print('[Android] Client connected')
        emit('server_response', {'message': f'[Android] Connected'}, broadcast=True)

    def on_disconnect(self, sid):
        emit('server_response', {'message': f'[Android] Disconnected: {sid}'}, broadcast=True)
        print('[Android] Client disconnected')

    def on_message(self, data):
        print(f"[Android] data: {data}")
        emit('server_response', {'message': f"[Android] {data}"}, broadcast=True)

    def on_client_message(self, data):
        print(f'[Android] data: {data}')
        emit('server_response', {'message': f"[Android] {data}"})