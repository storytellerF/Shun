package com.storyteller_f.filter_core.filter

import com.storyteller_f.filter_core.Filter
import java.util.regex.Pattern

interface RegExp {
    val regexp: String?
}

abstract class RegExpFilter<T>(showName: String) : Filter<T>(showName), RegExp {
    override fun filter(t: T): Boolean {
        val currentRegExp = regexp ?: return true
        return Pattern.compile(currentRegExp).matcher(getMatchString(t)).find()
    }

    /**
     * 获取被匹配的字符串
     *
     * @param t 当前对象
     * @return 返回被匹配的字符串
     */
    abstract fun getMatchString(t: T): CharSequence
}