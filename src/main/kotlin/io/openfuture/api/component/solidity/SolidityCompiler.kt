package io.openfuture.api.component.solidity

import org.springframework.stereotype.Component
import org.web3j.sokt.SolcArguments
import org.web3j.sokt.SolcInstance
import org.web3j.sokt.SolcOutput
import org.web3j.sokt.SolidityFile
import java.io.*

@Component
class SolidityCompiler {

    fun compileSolkt(filePath: String): SolcOutput {
        val compilerInstance = SolidityFile(filePath).getCompilerInstance()
        return compilerInstance.execute(
            SolcArguments.COMBINED_JSON.param { "abi,bin,interface,metadata" }
        )
    }

    fun compile(source: ByteArray): Result {

        val commandParts  = prepareCommand()
        commandParts.add("-")
        val process = ProcessBuilder(commandParts).start()
        BufferedOutputStream(process.outputStream).use { stream -> stream.write(source) }
        val error = ParallelReader(process.errorStream)
        val output = ParallelReader(process.inputStream)
        error.start()
        output.start()
        try {
            process.waitFor()
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
        val success = process.exitValue() == 0
        return Result(error.getContent(), output.getContent(), success)
    }

    private class ParallelReader(private var stream: InputStream?) : Thread() {
        private val content = StringBuilder()
        fun getContent(): String {
            return getContent(true)
        }

        @Synchronized
        fun getContent(waitForComplete: Boolean): String {
            return content.toString()
        }

        override fun run() {
            try {
                BufferedReader(InputStreamReader(stream)).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        content.append(line).append("\n")
                    }
                }
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            } finally {
                synchronized(this) {
                    stream = null
                }
            }
        }
    }

    class Result(var errors: String, var output: String, private val success: Boolean) {
        val isFailed: Boolean
            get() = !success

    }

    private fun prepareCommand(): MutableList<String> {
        val commandParts: MutableList<String> = ArrayList()
        commandParts.add("solc")
        commandParts.add("--combined-json=" + "abi,bin,interface,metadata")
        return commandParts
    }
}