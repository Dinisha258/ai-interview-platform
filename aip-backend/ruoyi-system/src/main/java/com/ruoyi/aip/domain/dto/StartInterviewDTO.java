package com.ruoyi.aip.domain.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 开始面试请求参数 DTO
 */
@Data
public class StartInterviewDTO {

    @NotNull(message = "岗位不能为空")
    private Long positionId;

    @NotNull(message = "目标等级不能为空")
    private Integer targetLevel; // 1-实习, 2-初级, 3-中级, 4-高级
}
