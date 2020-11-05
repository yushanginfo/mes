[#ftl]
[@b.head/]
[@b.toolbar title="订单信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">订单编号</td>
    <td class="content">${departAssess.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">生产批号</td>
    <td class="content">${departAssess.batchNum}</td>
  </tr>
  <tr>
    <td class="title" width="20%">产品图号</td>
    <td class="content">${(departAssess.product.code)!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">顾客代码</td>
    <td class="content">${(departAssess.product.customer.code)!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">订单数量</td>
    <td class="content">${departAssess.amount!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">计划交付日期</td>
    <td class="content">${(departAssess.requireOn?string("yyyy-MM-dd" ))!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">计划完工日期</td>
    <td class="content">${(departAssess.scheduledOn?string("yyyy-MM-dd" ))!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">订单状态</td>
    <td class="content">${departAssess.status.name!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">说明</td>
    <td class="content">${departAssess.remark!}</td>
  </tr>
</table>

[@b.foot/]
