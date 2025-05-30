from flask import Flask, render_template
from flask_socketio import SocketIO
from .handlers.android_handlers import AndroidNamespace
from .handlers.unity_handlers import UnityNamespace

socketio = SocketIO()

def create_app():
    app = Flask(__name__, static_folder='static', template_folder='templates')
    app.config['SECRET_KEY'] = 'secret!'
    
    # Initialize SocketIO
    socketio.init_app(app, cors_allowed_origins="*")
    
    # Register socket namespace
    socketio.on_namespace(AndroidNamespace('/android'))
    socketio.on_namespace(UnityNamespace('/unity'))

    socketio.emit('message', {'message':f'[Andoird] test'}, namespace='/unity')

    @app.route('/')
    def index():
        return render_template('index.html')

    return app