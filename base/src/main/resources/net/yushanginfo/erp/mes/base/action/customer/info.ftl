[#ftl]
[@b.head/]
[@b.toolbar title="用户信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%" >编号</td>
    <td class="content">${customer.code}</td>
  </tr>
  <tr>
    <td class="title">名称</td>
    <td class="content">${customer.name}</td>
  </tr>
  <tr>
    <td class="title">总公司</td>
    <td class="content">${(customer.parent.name)!'--'}</td>
  </tr>
  <tr>
    <td class="title">业务人员</td>
    <td class="content">${(customer.saler.department.name)!} ${(customer.saler.code)!} ${(customer.saler.name)!}</td>
  </tr>
  <tr>
    <td class="title">说明</td>
    <td class="content">${customer.remark!}</td>
  </tr>
</table>

[@b.foot/]
