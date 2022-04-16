[#ftl]
[@b.head/]
[@b.toolbar title="工艺路线"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">产品图号</td>
    <td class="content">${technicScheme.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">产品名称</td>
    <td class="content">${technicScheme.name}</td>
  </tr>
  <tr>
    <td class="title" width="20%">产品规格</td>
    <td class="content">${technicScheme.specification}</td>
  </tr>
  <tr>
    <td class="title" width="20%">工艺列表</td>
    <td class="content">
      [#list technicScheme.technics! as technic]
        ${technic.name}[#if technic_has_next],[/#if]
      [/#list]
    </td>
  </tr>
  <tr>
    <td class="title" width="20%">备注</td>
    <td class="content">${technicScheme.remark!}</td>
  </tr>
</table>

[@b.foot/]
