package com.moat.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class NicknameDTO {
  @NotNull
  @NotEmpty
  private String nickname;

  public NicknameDTO() {
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getNickname() {
    return nickname;
  }
}
