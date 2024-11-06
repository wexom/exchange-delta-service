package cz.wexom.eds.exception

class CurrencyNotSupportedByCurrencyApiException(from: String, to: String) : Exception("Currency pair is not supported by CurrencyApi: $from -> $to")
