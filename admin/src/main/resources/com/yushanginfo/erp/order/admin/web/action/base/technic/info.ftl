[#ftl]
[@b.head/]
[@b.toolbar title="工序基本信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">代码</td>
    <td class="content">${technic.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">名称</td>
    <td class="content">${technic.name}</td>
  </tr>
</table>

[@b.foot/]
