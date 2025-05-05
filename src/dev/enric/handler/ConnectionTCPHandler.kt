package dev.enric.handler

import dev.enric.util.Protocol
import dev.enric.util.RepositoryManager
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.Socket
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.random.Random

class ConnectionTCPHandler {
    fun acceptConnection(clientSocket: Socket) {
        clientSocket.getInputStream().bufferedReader().use { input ->
            val request = input.readLine()
            val matchResult = Protocol.validateRequest(request)

            if (matchResult != null) {
                val (_, _, _, _, path) = matchResult.destructured
                val repositoryPath = Paths.get(path)
                val redirectPort = Random.nextInt(7400, 7500)

                if (RepositoryManager.repositoryExists(repositoryPath)) {
                    executeTCPServe(repositoryPath, redirectPort)
                }

                responseToClient(clientSocket, matchResult, redirectPort)
            }
        }
    }

    private fun executeTCPServe(repositoryFolder: Path, port: Int) {
        val pb = ProcessBuilder("trackit", "tcp-serve", "--port", port.toString())
        pb.directory(repositoryFolder.toFile())
        pb.start()
    }

    private fun responseToClient(clientSocket: Socket, response: MatchResult?, redirectPort: Int) {
        val (username, password, ip, _, path) = response!!.destructured
        val redirectResponse = "tcp://$username:$password@$ip:$redirectPort/$path"

        val output = BufferedWriter(OutputStreamWriter(clientSocket.getOutputStream()))
        output.write(redirectResponse)
        output.flush()
        output.close()

        clientSocket.close()
    }
}