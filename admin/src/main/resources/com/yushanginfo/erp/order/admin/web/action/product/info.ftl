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
    <td class="title" width="20%">产品规格</td>
    <td class="content">${product.standard}</td>
  </tr>
  <tr>
    <td class="title" width="20%">客户编号</td>
    <td class="content">${product.customer.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">工艺列表</td>
    <td class="content">
      [#list product.technics! as technic]
        ${technic.name}[#if technic_has_next],[/#if]
      [/#list]
    </td>
  </tr>
  <tr>
    <td class="title" width="20%">备注</td>
    <td class="content">${product.remark!}</td>
  </tr>
</table>

[@b.foot/]
