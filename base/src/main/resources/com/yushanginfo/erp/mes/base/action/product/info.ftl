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
    <td class="title" width="20%">材料清单</td>
    <td class="content">
      [#list product.bom as m]
        ${m.indexno} ${m.material.code} ${m.material.name} ${m.material.specification} ${m.amount}[#if m_has_next]<br>[/#if]
      [/#list]
    </td>
  </tr>
  <tr>
    <td class="title" width="20%">工艺列表</td>
    <td class="content">
      [#list product.technicSchemes! as scheme]
        ${scheme.name}([#list scheme.technics as t]${t.technic.name}[#if t_has_next],[/#if][/#list])[#if scheme_has_next]<br>[/#if]
      [/#list]
    </td>
  </tr>
  <tr>
    <td class="title" width="20%">备注</td>
    <td class="content">${product.remark!}</td>
  </tr>
</table>

[@b.foot/]
