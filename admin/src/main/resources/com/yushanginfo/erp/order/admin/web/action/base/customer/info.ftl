[#ftl]
[@b.head/]
[@b.toolbar title="用户信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">编号</td>
    <td class="content">${customer.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">姓名</td>
    <td class="content">${customer.name}</td>
  </tr>
  <tr>
    <td class="title" width="20%">业务人员</td>
    <td class="content">
      [#list customer.salers! as saler]
        ${saler.name}[#if saler_has_next],[/#if]
      [/#list]
    </td>
  </tr>
  <tr>
    <td class="title" width="20%">说明</td>
    <td class="content">${customer.remark!}</td>
  </tr>
</table>

[@b.foot/]
