package utils

import java.io.File

import play.Logger

/**
 * User: Kayrnt
 * Date: 19/10/14
 * Time: 16:45
 */
case class FlowInfo(resumableChunkSize: Int,
                         resumableTotalSize: Long = 0L,
                         resumableIdentifier: String = null,
                         resumableFilename: String = null,
                         resumableRelativePath: String = null,
                         resumableFilePath: String = null) {

  type ResumableChunckNumber = Int

  var uploadedChunks: Set[Int] = Set[ResumableChunckNumber]()

  def valid: Boolean = {
    if (resumableChunkSize < 0 || resumableTotalSize < 0 || resumableIdentifier.isEmpty || resumableFilename.isEmpty || resumableRelativePath.isEmpty) false
    else true
  }

  def checkIfUploadFinished: Boolean = {
    //check if upload finished
    val count: Int = Math.ceil(resumableTotalSize.toDouble / resumableChunkSize.toDouble).toInt
    println("resumable ts :"+resumableTotalSize)
    println("resumable cs :"+resumableChunkSize)
    println("count :"+count)
    (1 to count).map {
      i =>
        println("i >"+i)
        if (!uploadedChunks.contains(i)) {
          return false
        }
    }
    println(">")
    //Upload finished, change filename.
    val file: File = new File(resumableFilePath)
    val newPath: String = file.getAbsolutePath.substring(0, file.getAbsolutePath.length - ".temp".length)
    println("going to rename")
    file.renameTo(new File(newPath))
    Logger.info("file renamed to "+newPath)
    return true
  }
}
