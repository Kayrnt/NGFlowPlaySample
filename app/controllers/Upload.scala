package controllers

import java.io._

import play.api.Logger
import play.api.libs.Files.TemporaryFile
import play.api.mvc.{MaxSizeExceeded, _}
import utils.{FlowHelper, FlowInfoStorage, FlowInfo}

/**
 * User: Kayrnt
 * Date: 19/10/14
 * Time: 00:04
 */

object Upload extends Controller with FlowHelper {

  //we want to parse only up to 5MB
  val multipartMaxLengthParser = parse.maxLength(1024 * 5000, parse.multipartFormData)

  def upload = Action(multipartMaxLengthParser) {
    implicit request =>
      Logger.info("request accepted")
      request.body.fold(sizeExceeded, sizeAccepted)
  }

  private def sizeExceeded(size: MaxSizeExceeded) = {
    Logger.info("File size exceeded " + size.length)
    BadRequest("File size exceeded")
  }

  private def sizeAccepted(multipart: MultipartFormData[TemporaryFile])(implicit request: RequestHeader) = {
    multipart.file("file").map { picture =>
      Logger.info("using file")
      //checking type, we want only pictures
      picture.contentType match {
        case Some("image/jpeg") | Some("image/png") =>
          Logger.info("content type matched")
          val is = new FileInputStream(picture.ref.file)
          dealWithFile(is, multipart)
        case _ => BadRequest("invalid content type")
      }
    }.getOrElse {
      BadRequest("Missing file")
    }
  }

  def dealWithFile(is: InputStream, multipart: MultipartFormData[TemporaryFile])(implicit request: RequestHeader): Result = {
    val flowChunkNumber: Int = getFlowChunkNumberMultipart(multipart)
    val info: FlowInfo = getFlowInfoMultipart(multipart)
    val contentLength: Long = request.headers("Content-Length").toLong
    writeInTempFile(flowChunkNumber, info, contentLength, is)
    info.uploadedChunks += flowChunkNumber
    if (info.checkIfUploadFinished) {
      FlowInfoStorage.remove(info)
      println("file upload finished")
      Ok("All finished.")
    }
    else {
      Ok("Upload")
    }
  }

  def uploadGet() = Action {
    request =>
      val flowChunkNumber: Int = getFlowChunkNumber(request)
      val info: FlowInfo = getFlowInfo(request)
      if (info.uploadedChunks.contains(flowChunkNumber)) {
        Ok
      }
      else {
        NotFound
      }
  }

  

}
