[#ftl]
[@b.head/]
[@b.toolbar title="工单信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">工单编号</td>
    <td class="content">${workOrder.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">生产批号</td>
    <td class="content">${workOrder.batchNum}</td>
  </tr>
  <tr>
    <td class="title" width="20%">产品图号</td>
    <td class="content">${(workOrder.product.code)!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">顾客代码</td>
    <td class="content">${(workOrder.product.customer.code)!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">工单数量</td>
    <td class="content">${workOrder.amount!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">客户交期</td>
    <td class="content">${(workOrder.deadline?string("yyyy-MM-dd" ))!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">到料日期</td>
    <td class="content">${(workOrder.materialDate?string("yyyy-MM-dd" ))!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">部门评审</td>
    <td class="content">

    </td>
  </tr>
  <tr>
    <td class="title" width="20%">评审交期</td>
    <td class="content">${(workOrder.scheduledOn?string("yyyy-MM-dd" ))!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">工单状态</td>
    <td class="content">${workOrder.status.name!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">说明</td>
    <td class="content">${workOrder.remark!}</td>
  </tr>
</table>

[@b.foot/]
