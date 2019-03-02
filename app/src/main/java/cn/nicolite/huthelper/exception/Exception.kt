package cn.nicolite.huthelper.exception

/**
 * Created by nicolite on 2018/7/8.
 * email nicolite@nicolite.cn
 */
class APIException : Exception {
    var code: Int = 0
    var msg: String = ""

    constructor(exceptionError: ExceptionError) {
        this.code = exceptionError.code
        this.msg = exceptionError.msg
    }

    constructor(code: Int, msg: String) {
        this.code = code
        this.msg = msg
    }


    override fun toString(): String {
        return "code: $code msg: $msg"
    }
}