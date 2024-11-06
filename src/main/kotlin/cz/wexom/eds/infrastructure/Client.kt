package cz.wexom.eds.infrastructure

interface Client {
    suspend fun isHealthy(): Boolean
}