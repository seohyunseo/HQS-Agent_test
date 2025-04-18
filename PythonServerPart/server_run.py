import server

app = server.create_app()

if __name__ == '__main__':
    server.socketio.run(app, host='0.0.0.0', port=5000)