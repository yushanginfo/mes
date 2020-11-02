[#ftl]
[@b.head/]
[@b.toolbar title="产品信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">产品图号</td>
    <td class="content">${product.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">产品名称</td>
    <td class="content">${product.name}</td>
  </tr>
  <tr>
    <td class="title" width="20%">产品图号</td>
    <td class="content">${product.specification!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">工艺列表</td>
    <td class="content">
      [#list product.technicSchemes! as scheme]
        ${scheme.name}[#if scheme_has_next],[/#if]
      [/#list]
    </td>
  </tr>
  <tr>
    <td class="title" width="20%">备注</td>
    <td class="content">${product.remark!}</td>
  </tr>
</table>

[@b.foot/]
