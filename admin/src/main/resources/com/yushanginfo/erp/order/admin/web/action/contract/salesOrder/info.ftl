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
    <td class="title">品号</td>
    <td class="content">${(salesOrder.product.code)!}</td>
    <td class="title">订单类型</td>
    <td class="content">${(salesOrder.orderType.name)!}</td>
  </tr>
  <tr>
    <td class="title">产品图号</td>
    <td class="content">${(salesOrder.product.specification)!}</td>
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
    <td class="title">客户交期</td>
    <td class="content">${(salesOrder.deadline?string("yyyy-MM-dd" ))!}</td>
    <td class="title">计划交期</td>
    <td class="content">${(salesOrder.plannedEndOn?string("yyyy-MM-dd" ))!}</td>
  </tr>
  <tr>
    <td class="title">到料日期</td>
    <td class="content">${(salesOrder.materialDate?string("yyyy-MM-dd" ))!}</td>
    <td class="title">评审交期</td>
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
    <td class="title">评审信息</td>
    <td class="content"  colspan="3">
    [#assign assessMap={}/]
[#list salesOrder.assesses as assess]
     [#assign assessMap=assessMap+{assess.technic.id?string:assess}]
[/#list]
      [#list salesOrder.technicScheme.technics as pt]
         ${pt.technic.name}(${pt.description!})
         [#if assessMap[pt.technic.id?string]??]
         [#assign assess=assessMap[pt.technic.id?string]/]
         ${assess.factory.name} ${assess.days}天 <span style="font-size:0.8rem;color: #999;">${(assess.assessedBy.name)!} ${assess.updatedAt?string("yyyy-MM-dd HH:mm")}</span>
         [#else]尚未评审[/#if] [#if pt_has_next]<br>[/#if]
      [/#list]
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
