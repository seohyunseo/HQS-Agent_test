import hqs_server

app = hqs_server.create_app()

if __name__ == '__main__':
    hqs_server.socketio.run(app, host='0.0.0.0', port=5000)