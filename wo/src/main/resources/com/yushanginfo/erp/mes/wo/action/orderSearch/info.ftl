[#ftl]
[@b.head/]
[@b.toolbar title="工单信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title"  width="20%">工单编号</td>
    <td class="content">${workOrder.code}</td>
    <td class="title"  width="20%">生产批号</td>
    <td class="content">${workOrder.batchNum}</td>
  </tr>
  <tr>
    <td class="title">品号</td>
    <td class="content">${(workOrder.product.code)!}</td>
    <td class="title">工单类型</td>
    <td class="content">${(workOrder.orderType.name)!}</td>
  </tr>
  <tr>
    <td class="title">产品图号</td>
    <td class="content">${(workOrder.product.specification)!}</td>
    <td class="title">客户信息</td>
    <td class="content">${(workOrder.product.customer.code)!} ${(workOrder.product.customer.name)!}</td>
  </tr>
  <tr>
    <td class="title">工单数量</td>
    <td class="content">${workOrder.amount!}</td>
    <td class="title">所在厂区</td>
    <td class="content">${workOrder.factory.name}</td>
  </tr>
  <tr>
    <td class="title">客户交期</td>
    <td class="content">${(workOrder.deadline?string("yyyy-MM-dd" ))!}</td>
    <td class="title">计划交期</td>
    <td class="content">${(workOrder.plannedEndOn?string("yyyy-MM-dd" ))!}</td>
  </tr>
  <tr>
    <td class="title">到料日期</td>
    <td class="content">${(workOrder.materialDate?string("yyyy-MM-dd" ))!}</td>
    <td class="title">评审交期</td>
    <td class="content">${(workOrder.scheduledOn?string("yyyy-MM-dd" ))!}</td>
  </tr>
  <tr>
    <td class="title">材料清单</td>
    <td class="content" colspan="3">
      [#list workOrder.product.bom as m]
        ${m.indexno} ${m.material.code} ${m.material.name} ${m.material.specification} ${m.amount}[#if m_has_next]<br>[/#if]
      [/#list]
    </td>
  </tr>
  <tr>
    <td class="title">工艺列表</td>
    <td class="content"  colspan="3">
      [#assign scheme  = workOrder.technicScheme]
      ${scheme.name}([#list scheme.technics as t]${t.technic.name}[#if t_has_next],[/#if][/#list])
    </td>
  </tr>
  <tr>
    <td class="title">评审信息</td>
    <td class="content"  colspan="3">
    [#assign assessMap={}/]
[#list workOrder.assesses as assess]
     [#assign assessMap=assessMap+{assess.technic.id?string:assess}]
[/#list]
      [#list workOrder.technicScheme.technics as pt]
         ${pt.technic.name}(${pt.description!})
         [#if assessMap[pt.technic.id?string]??]
         [#assign assess=assessMap[pt.technic.id?string]/]
         ${assess.factory.name} ${assess.days}天 <span style="font-size:0.8rem;color: #999;">${(assess.assessedBy.name)!} ${assess.updatedAt?string("yyyy-MM-dd HH:mm")}</span>
         [#else]尚未评审[/#if] [#if pt_has_next]<br>[/#if]
      [/#list]
    </td>
  </tr>
  <tr>
    <td class="title">工单状态</td>
    <td class="content">${workOrder.status.name!}</td>
    <td class="title">说明</td>
    <td class="content">${workOrder.remark!}</td>
  </tr>
</table>

[@b.foot/]
