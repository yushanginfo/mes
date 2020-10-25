[#ftl]
[@b.head/]
[@b.toolbar title="部门基本信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">代码</td>
    <td class="content">${department.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">名称</td>
    <td class="content">${department.name}</td>
  </tr>
  <tr>
    <td class="title" width="20%">序号</td>
    <td class="content">${department.indexno}</td>
  </tr>
  <tr>
    <td class="title" width="20%">上级部门</td>
    <td class="content">${(department.parent.name)!}</td>
  </tr>
</table>

[@b.foot/]
