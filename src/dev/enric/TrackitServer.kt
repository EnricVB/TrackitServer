package dev.enric

import dev.enric.handler.ConnectionTCPHandler
import java.net.ServerSocket

//TODO: Use SSL instead of TCP and make port 8088 configurable per Computer
fun main() {
    val serverSocket = ServerSocket(8088)

    while (true) {
        val clientSocket = serverSocket.accept()

        Thread {
            ConnectionTCPHandler().acceptConnection(clientSocket)
        }.start()
    }
}