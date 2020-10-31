[#ftl]
[@b.head/]
[@b.toolbar title="产品信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">产品图号</td>
    <td class="content">${material.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">产品名称</td>
    <td class="content">${material.name}</td>
  </tr>
  <tr>
    <td class="title" width="20%">产品规格</td>
    <td class="content">${material.specification}</td>
  </tr>
  <tr>
    <td class="title" width="20%">品号类别</td>
    <td class="content">${material.materialType.name}</td>
  </tr>
  <tr>
    <td class="title" width="20%">备注</td>
    <td class="content">${material.remark!}</td>
  </tr>
</table>

[@b.foot/]
