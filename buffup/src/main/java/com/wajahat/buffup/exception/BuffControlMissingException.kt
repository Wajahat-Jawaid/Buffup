package com.wajahat.buffup.exception

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
/** Buff Controls are dependent upon the StreamDetails data. If any of the objects in
 * details is invalid, control won't be generated and hence respective control exception
 * is thrown */
class BuffControlMissingException(throwable: Throwable) : Exception(throwable)