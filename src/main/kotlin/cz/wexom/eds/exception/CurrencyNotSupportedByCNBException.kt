package cz.wexom.eds.exception

class CurrencyNotSupportedByCNBException(from: String, to: String) : Exception("Currency pair is not supported by CNB: $from -> $to")
