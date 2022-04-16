[#ftl]
[@b.head/]
[@b.toolbar title="工艺基本信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">代码</td>
    <td class="content">${technic.code}</td>
    <td class="title" width="20%">名称</td>
    <td class="content">${technic.name}</td>
  </tr>
  <tr>
    <td class="title" width="20%">性质</td>
    <td class="content"> [#if technic.internal]厂内[#else]委外[/#if]</td>
    <td class="title" width="20%">加工中心</td>
    <td class="content">${(technic.machine.name)!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">说明</td>
    <td class="content" colspan="3">${(technic.description)!}</td>
  </tr>
</table>

[@b.foot/]
