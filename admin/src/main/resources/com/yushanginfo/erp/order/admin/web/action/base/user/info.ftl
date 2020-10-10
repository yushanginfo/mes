[#ftl]
[@b.head/]
[@b.toolbar title="用户信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">工号</td>
    <td class="content">${user.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">姓名</td>
    <td class="content">${user.name}</td>
  </tr>
  <tr>
    <td class="title" width="20%">所在部门</td>
    <td class="content">${(user.department.name)!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">电话</td>
    <td class="content">${user.mobile!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">邮箱</td>
    <td class="content">${user.email!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">说明</td>
    <td class="content">${user.remark!}</td>
  </tr>
</table>

[@b.foot/]
