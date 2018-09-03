package org.infrastructure.common.web.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class FieldErrorDTO {
  @ApiModelProperty(value = "字段名称", name = "字段名称")
  private final String field;
  @ApiModelProperty(value = "字段错误描述", name = "字段错误描述")
  private final String message;

  public FieldErrorDTO(final String field, final String message) {
    this.field = field;
    this.message = message;
  }

  // API
  public String getField() {
    return field;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public final String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("FieldError [field=").append(field).append(", message=").append(message)
        .append("]");
    return builder.toString();
  }

}
