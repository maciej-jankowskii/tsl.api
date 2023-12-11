package com.tsl.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsDTO {
    private Long id;
    @NotBlank(message = "Name cannot be null")
    private String name;
    @NotBlank(message = "Type of goods cannot be null")
    private String typeOfGoods;
    @NotBlank(message = "County cannot be null")
    @Min(1)
    private Double quantity;
    @NotBlank(message = "Label cannot be null")
    private String label;
    private String description;
    @NotBlank(message = "Required Area cannot be null")
    @Min(1)
    private Double requiredArea;
    private Boolean assignedToOrder;
}
