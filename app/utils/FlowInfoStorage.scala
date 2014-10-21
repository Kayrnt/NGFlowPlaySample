package utils

import scala.collection.mutable

/**
 * User: Kayrnt
 * Date: 19/10/14
 * Time: 17:10
 */

object FlowInfoStorage {
  private var mMap: mutable.HashMap[String, FlowInfo] = new mutable.HashMap[String, FlowInfo]()

  /**
   * Get ResumableInfo from mMap or Create a new one.
   * @param resumableChunkSize
   * @param resumableTotalSize
   * @param resumableIdentifier
   * @param resumableFilename
   * @param resumableRelativePath
   * @param resumableFilePath
   * @return
   */
  def get(resumableChunkSize: Int, resumableTotalSize: Long, resumableIdentifier: String, resumableFilename: String, resumableRelativePath: String, resumableFilePath: String): FlowInfo =
    mMap.getOrElse(resumableIdentifier, {
      val ri = FlowInfo(resumableChunkSize, resumableTotalSize, resumableIdentifier, resumableFilename, resumableRelativePath, resumableFilePath)
      mMap += (ri.resumableIdentifier -> ri)
      ri
    })

  /**
   * ResumableInfo
   * @param info
   */
  def remove(info: FlowInfo) =  mMap -= info.resumableIdentifier
}
