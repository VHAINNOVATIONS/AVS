package gov.va.med.lom.avs.model;

import java.io.Serializable;

public class RoleAdminScreen implements Serializable {

  private Long roleId;
  private String roleName;
  private String roleDescription;
  private Long adminScreenId;
  private String adminScreen;
  
  public Long getRoleId() {
    return roleId;
  }
  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }
  public String getRoleName() {
    return roleName;
  }
  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }
  public String getRoleDescription() {
    return roleDescription;
  }
  public void setRoleDescription(String roleDescription) {
    this.roleDescription = roleDescription;
  }
  public Long getAdminScreenId() {
    return adminScreenId;
  }
  public void setAdminScreenId(Long adminScreenId) {
    this.adminScreenId = adminScreenId;
  }
  public String getAdminScreen() {
    return adminScreen;
  }
  public void setAdminScreen(String adminScreen) {
    this.adminScreen = adminScreen;
  }

}