[#ftl]
[@b.head/]
[@b.toolbar title="部门基本信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">编码</td>
    <td class="content">${factory.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">名称</td>
    <td class="content">${factory.name}</td>
  </tr>
  <tr>
    <td class="title" width="20%">地址</td>
    <td class="content">${factory.address!}</td>
  </tr>
</table>

[@b.foot/]
