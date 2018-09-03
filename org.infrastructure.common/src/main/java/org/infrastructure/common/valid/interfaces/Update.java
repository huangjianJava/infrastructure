/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: Update
 * Author:   temp
 * Date:     2018/4/26 10:51
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package org.infrastructure.common.valid.interfaces;

import javax.validation.GroupSequence;

/**
 * 针对修改验证<br>
 *     First 修改插入共享验证
 *     UpdateBy 修改独享验证
 * 〈〉
 *
 * @author temp
 * @create 2018/4/26
 * @since 1.0.0
 */
@GroupSequence({First.class,UpdateBy.class})
public interface Update {

}