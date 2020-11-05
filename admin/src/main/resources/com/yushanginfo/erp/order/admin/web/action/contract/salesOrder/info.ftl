[#ftl]
[@b.head/]
[@b.toolbar title="订单信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title"  width="20%">订单编号</td>
    <td class="content">${salesOrder.code}</td>
    <td class="title"  width="20%">生产批号</td>
    <td class="content">${salesOrder.batchNum}</td>
  </tr>
  <tr>
    <td class="title">产品图号</td>
    <td class="content">${(salesOrder.product.code)!}</td>
    <td class="title">客户信息</td>
    <td class="content">${(salesOrder.product.customer.code)!} ${(salesOrder.product.customer.name)!}</td>
  </tr>
  <tr>
    <td class="title">订单数量</td>
    <td class="content">${salesOrder.amount!}</td>
    <td class="title">所在厂区</td>
    <td class="content">${salesOrder.factory.name}</td>
  </tr>
  <tr>
    <td class="title">计划交付日期</td>
    <td class="content">${(salesOrder.requireOn?string("yyyy-MM-dd" ))!}</td>
    <td class="title">计划完工日期</td>
    <td class="content">${(salesOrder.scheduledOn?string("yyyy-MM-dd" ))!}</td>
  </tr>
  <tr>
    <td class="title">材料清单</td>
    <td class="content" colspan="3">
      [#list salesOrder.product.bom as m]
        ${m.indexno} ${m.material.code} ${m.material.name} ${m.material.specification} ${m.amount}[#if m_has_next]<br>[/#if]
      [/#list]
    </td>
  </tr>
  <tr>
    <td class="title">工艺列表</td>
    <td class="content"  colspan="3">
      [#assign scheme  = salesOrder.technicScheme]
      ${scheme.name}([#list scheme.technics as t]${t.technic.name}[#if t_has_next],[/#if][/#list])
    </td>
  </tr>
  <tr>
    <td class="title">订单状态</td>
    <td class="content">${salesOrder.status.name!}</td>
    <td class="title">说明</td>
    <td class="content">${salesOrder.remark!}</td>
  </tr>
</table>

[@b.foot/]
