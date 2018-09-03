/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: Query
 * Author:   temp
 * Date:     2018/4/26 10:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package org.infrastructure.common.valid.interfaces;

import javax.validation.GroupSequence;

/**
 * 查询验证分组<br>
 * 〈〉
 *
 * @author temp
 * @create 2018/4/26
 * @since 1.0.0
 */
@GroupSequence({First.class,Second.class})
public interface Query {

}