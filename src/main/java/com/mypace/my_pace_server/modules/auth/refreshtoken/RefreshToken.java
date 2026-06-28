package com.mypace.my_pace_server.modules.auth.refreshtoken;

import java.util.Date;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "refresh_tokens")
public class RefreshToken {
  @Id private String id;
  private String jti;
  private String userId;
  private Date createdAt;
  private Date expiresAt;
  private boolean revoked;
  private String replacedByToken;
}
