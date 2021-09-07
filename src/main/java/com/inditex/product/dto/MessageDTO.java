package com.inditex.product.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class MessageDTO {
  @NonNull private String mensaje;
}
