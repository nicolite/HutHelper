package cn.nicolite.huthelper.model.wrapper

/**
 * Created by nicolite on 2018/6/27.
 * email nicolite@nicolite.cn
 */
class RestResult<T>(
        val code: Int,       //错误码
        val msg: String,     //提示信息
        val data: T  //数据
) {

    override fun toString(): String {
        return "code: " + code + " msg: " + msg + " object: " + super.toString()
    }
}
