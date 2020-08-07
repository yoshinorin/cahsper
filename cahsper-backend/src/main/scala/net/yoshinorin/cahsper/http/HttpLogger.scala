package net.yoshinorin.cahsper.http

import akka.event.Logging
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.{Directive0, RouteResult}
import akka.http.scaladsl.server.directives.{DebuggingDirectives, LogEntry}

trait HttpLogger {

  // TODO: Change log format
  private[this] def requestAndResponseLogging(request: HttpRequest): RouteResult => Option[LogEntry] = {
    case RouteResult.Complete(response) =>
      Some(LogEntry(request.method.name + " - " + request.uri + " - " + response.status, Logging.InfoLevel))
    case RouteResult.Rejected(rejections) =>
      Some(LogEntry(request.method.name + " - " + request.uri + " - " + rejections, Logging.ErrorLevel))
  }

  def httpLogging: Directive0 = DebuggingDirectives.logRequestResult(requestAndResponseLogging _)

}
