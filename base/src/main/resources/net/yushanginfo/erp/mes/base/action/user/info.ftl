[#ftl]
[@b.head/]
[@b.toolbar title="用户信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title"  width="20%">工号</td>
    <td class="content">${user.code}</td>
    <td class="title">姓名</td>
    <td class="content">${user.name}</td>
  </tr>
  <tr>
    <td class="title">所在部门</td>
    <td class="content">${(user.department.name)!}</td>
    <td class="title">所在厂区</td>
    <td class="content">${(user.factory.name)!}</td>
  </tr>
  <tr>
    <td class="title">电话</td>
    <td class="content">${user.mobile!}</td>
    <td class="title">邮箱</td>
    <td class="content">${user.email!}</td>
  </tr>
  <tr>
    <td class="title">说明</td>
    <td class="content" colspan="3">${user.remark!}</td>
  </tr>
</table>

[@b.foot/]
