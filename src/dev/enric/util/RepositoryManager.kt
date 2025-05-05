package dev.enric.util

import java.nio.file.Path

class RepositoryManager {
    companion object {
        fun repositoryExists(repositoryFolder: Path): Boolean {
            val repositoryPath = repositoryFolder.resolve(".trackit")

            return repositoryPath.toFile().exists() && repositoryPath.toFile().isDirectory
        }
    }
}