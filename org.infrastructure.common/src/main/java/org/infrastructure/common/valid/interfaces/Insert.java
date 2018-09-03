/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: Insert
 * Author:   temp
 * Date:     2018/4/26 13:58
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package org.infrastructure.common.valid.interfaces;

import javax.validation.GroupSequence;

/**
 * 插入验证分组<br>
 *     First 第一验证与Update共享验证
 *     InsertBy 第二验证插入独享
 *
 * 〈〉
 *
 * @author temp
 * @create 2018/4/26
 * @since 1.0.0
 */
@GroupSequence({First.class,InsertBy.class})
public interface Insert {

}