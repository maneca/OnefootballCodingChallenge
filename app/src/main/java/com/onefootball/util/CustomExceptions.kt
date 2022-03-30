package com.onefootball.util

sealed class CustomExceptions{

    object UnableToReadFile : CustomExceptions()

    object UnknownException : CustomExceptions()
}
