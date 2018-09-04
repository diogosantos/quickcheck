package com.quickcheck.messages

sealed trait QCRequest

case class SaveCheckRequest(message: String) extends QCRequest

case class ReverseStringRequest(message: String) extends QCRequest

case class InvalidReversibleStringException(something: Any) extends Exception