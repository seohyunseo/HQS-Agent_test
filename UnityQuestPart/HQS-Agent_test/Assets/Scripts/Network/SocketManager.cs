using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using SocketIOClient;
using SocketIOClient.Newtonsoft.Json;
using UnityEngine.UI;
using Newtonsoft.Json.Linq;
using System;
using UnityEditor.Experimental.GraphView;


public class SocketManager : MonoBehaviour
{
    [Header("Network Settings")]
    public SocketIOUnity socket;
    public string serverIp = "192.168.0.151";
    public int serverPort = 5000;

    private bool isInitialized = false;
    private string msg = "";

    private void Start()
    {
        InitializeSocket(serverIp, serverPort.ToString());
    }

    private void Update()
    {
        if (Input.GetKeyDown(KeyCode.Space))
        {
            if (isInitialized)
            {
                socket.Emit("message", "Data test from Unity");
            }
            
        }
    }

    private void InitializeSocket(string ip, string port)
    {
        if(socket != null)
        {
            try
            {
                socket.Disconnect();
                socket = null;
            }
            catch(Exception e)
            {
                Debug.Log("[SocketManager] Error disconnecting socket: " + e.Message);
            }
        }

        serverIp = ip;
        serverPort = int.Parse(port);

        var uri = new Uri("http://" + serverIp + ":" + serverPort + "/unity");
        Debug.Log("[SocketManager] Socket Initialized: " + uri);

        socket = new SocketIOUnity(uri);

        socket.OnConnected += (sender, e) =>
        {
            Debug.Log("[SocketManager] socket.OnConnected");
        };

        socket.OnPing += (sender, e) =>
        {
            Debug.Log("[SocketManager] socket.Ping");
        };
        socket.OnPong += (sender, e) =>
        {
            Debug.Log("[SocketManager] socket.Pong");
        };
        socket.OnDisconnected += (sender, e) =>
        {
            Debug.Log("[SocketManager] socket.OnDisconnected");
        };
        socket.OnReconnectAttempt += (sender, e) =>
        {
            Debug.Log("[SocketManager] socket.OnReconnectAttempt");
        };

        socket.Connect();
        isInitialized = true;

        SetupSocketEvents();
    }

    void OnDestroy()
    {
        if (socket != null)
        {
            Debug.Log("[SocketManager] Cleaning up socket...");
            try
            {
                // Disconnect regardless of connection state
                socket.Disconnect();
                socket = null;
                Debug.Log("[SocketManager] Socket disconnected");
            }
            catch (Exception e)
            {
                Debug.LogError("[SocketManager] Error during socket cleanup: " + e.Message);
            }
        }

        isInitialized = false;
    }

    public void Emit(string eventName, string data)
    {
        socket.Emit(eventName, data);
    }

    private void SetupSocketEvents()
    {
        socket.OnUnityThread("message", (data) =>
        {
            msg = data.GetValue<string>();
            Debug.Log("[SocketManager] Received from server: "+ msg);
        });
    }
}
